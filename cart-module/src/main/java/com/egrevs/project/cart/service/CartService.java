package com.egrevs.project.cart.service;

import com.egrevs.project.domain.entity.cart.Cart;
import com.egrevs.project.domain.entity.cart.CartItems;
import com.egrevs.project.domain.entity.restaurant.MenuItems;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.repository.UserRepository;
import com.egrevs.project.domain.repository.cartNorders.CartItemsRepository;
import com.egrevs.project.domain.repository.cartNorders.CartsRepository;
import com.egrevs.project.domain.repository.restaurant.MenuItemsRepository;
import com.egrevs.project.shared.dtos.cart.CartDto;
import com.egrevs.project.shared.dtos.cart.CartItemsDto;
import com.egrevs.project.shared.dtos.cart.CreateCartRequest;
import com.egrevs.project.shared.dtos.cart.UpdateCartItemsRequest;
import com.egrevs.project.shared.exceptions.cartNorders.CartNotFoundException;
import com.egrevs.project.shared.exceptions.restaurant.DishNotFoundException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import com.egrevs.project.shared.mapper.CartItemsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartsRepository cartRepository;
    private final CartItemsRepository cartItemRepository;
    private final UserRepository userRepository;
    private final MenuItemsRepository menuItemsRepository;

    @Transactional
    public CartDto createCart(CreateCartRequest cartRequest) {
        User user = userRepository.findById(cartRequest.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + cartRequest.userId() + " not found"));

        Cart cart = new Cart();
        cart.setUser(user);

        List<CartItems> cartItemsList = cartRequest.requestList().stream().map(itemReq -> {
            MenuItems menuItem = menuItemsRepository.findById(itemReq.menuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item with id " + itemReq.menuItemId() + " not found"));

            CartItems cartItems = new CartItems();
            cartItems.setCart(cart);
            cartItems.setMenuItems(menuItem);
            cartItems.setMenuItemName(menuItem.getName());
            cartItems.setMenuItemPrice(menuItem.getPrice());
            cartItems.setQuantity(itemReq.quantity());
            cartItems.setCreatedAt(LocalDateTime.now());
            cartItems.setTotalPrice(menuItem.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity())));
            return cartItems;
        }).toList();

        cart.setDishes(cartItemsList);

        cart.setQuantity(cartItemsList
                .stream()
                .mapToInt(CartItems::getQuantity)
                .sum());

        cart.setTotalPrice(cartItemsList
                .stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        Cart savedCart = cartRepository.save(cart);
        return toDto(savedCart);
    }

    @Transactional
    public void deleteDishFromCart(String itemId){
        CartItems cartItems = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DishNotFoundException("No dish in cart with such id"));

        cartItemRepository.delete(cartItems);
    }

    @Transactional
    public CartItemsDto changeItemQuantity(String itemId, UpdateCartItemsRequest request){
        CartItems cartItems = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DishNotFoundException("No dish in cart with such id"));

        cartItems.setQuantity(request.quantity());

        CartItems savedItem = cartItemRepository.save(cartItems);

        return CartItemsMapper.toDto(savedItem);
    }

    @Transactional
    public CartDto getUserCart(String userId){
        return toDto(cartRepository.findByUserId(userId));
    }

    @Transactional
    public void clearCart(String cartId){
        cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("No cart with such id"));

        cartItemRepository.deleteAllByCartId(cartId);
    }

    private CartDto toDto(Cart cart){
        return new CartDto(
                cart.getId(),
                cart.getUser().getId(),
                cart.getQuantity(),
                cart.getTotalPrice(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getDishes().stream().map(CartItemsMapper::toDto).collect(Collectors.toList())
        );
    }
}