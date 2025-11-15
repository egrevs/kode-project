package com.egrevs.project.cart.service;

import com.egrevs.project.cart.dto.cart.CartDto;
import com.egrevs.project.cart.dto.cart.CartItemsDto;
import com.egrevs.project.cart.dto.cart.CreateCartRequest;
import com.egrevs.project.cart.dto.cart.UpdateCartItemsRequest;
import com.egrevs.project.cart.entity.cart.Cart;
import com.egrevs.project.cart.entity.cart.CartItems;
import com.egrevs.project.cart.exception.CartNotFoundException;
import com.egrevs.project.cart.repository.CartItemsRepository;
import com.egrevs.project.cart.repository.CartsRepository;
import com.egrevs.project.catalog.exceptions.DishNotFoundException;
import com.egrevs.project.catalog.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartsRepository cartRepository;
    private final CartItemsRepository cartItemRepository;

    public CartDto createCart(CreateCartRequest cartRequest, String userId){
        Cart cart = new Cart();
        cart.setId(userId);

        List<CartItems> cartItemsList = cartRequest.requestList().stream().map(itemReq -> {
            CartItems cartItems = new CartItems();
            cartItems.setDishId(itemReq.dishId());
            cartItems.setCart(cart);
            cartItems.setDishPrice(itemReq.price());
            cartItems.setCreatedAt(LocalDateTime.now());
            cartItems.setQuantity(itemReq.quantity());
            cartItems.setTotalPrice(itemReq.price().multiply(BigDecimal.valueOf(itemReq.quantity())));
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

    public void deleteDishFromCart(String itemId){
        CartItems cartItems = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DishNotFoundException("No dish in cart with such id"));

        cartItemRepository.delete(cartItems);
    }

    public CartItemsDto changeItemQuantity(String itemId, UpdateCartItemsRequest request){
        CartItems cartItems = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new DishNotFoundException("No dish in cart with such id"));

        cartItems.setQuantity(request.quantity());

        CartItems savedItem = cartItemRepository.save(cartItems);

        return toDto(savedItem);
    }

    public CartDto getUserCart(String userId){
        return toDto(cartRepository.findByUserId(userId));
    }

    public void clearCart(String cartId){
        cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("No cart with such id"));

        cartItemRepository.deleteAllByCartId(cartId);
    }

    private CartDto toDto(Cart cart){
        return new CartDto(
                cart.getId(),
                cart.getUserId(),
                cart.getQuantity(),
                cart.getTotalPrice(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getDishes().stream().map(this::toDto).collect(Collectors.toList())
        );
    }

    private CartItemsDto toDto(CartItems cartItem){
        return new CartItemsDto(
                cartItem.getId(),
                cartItem.getDishPrice(),
                cartItem.getDishName(),
                cartItem.getTotalPrice(),
                cartItem.getQuantity(),
                cartItem.getCreatedAt()
        );
    }

}