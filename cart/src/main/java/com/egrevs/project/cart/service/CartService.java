package com.egrevs.project.cart.service;

import com.egrevs.project.cart.dto.*;
import com.egrevs.project.cart.entity.Cart;
import com.egrevs.project.cart.entity.CartItems;
import com.egrevs.project.cart.repository.CartItemsRepository;
import com.egrevs.project.cart.repository.CartsRepository;
import com.egrevs.project.catalog.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartsRepository cartRepository;
    private final CartItemsRepository cartItemRepository;
    private final RestaurantService restaurantService;

    @Transactional(readOnly = true)
    public CartDto getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found: " + cartId));
        return toDto(cart);
    }

    @Transactional
    public CartDto createCart(CreateCartRequest request) {
        Cart cart = new Cart();
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());

        for (CreateCartItemsRequest itemReq : request.requestList()) {
            CartItems item = new CartItems();
            item.setCart(cart);
            item.setDishId(itemReq.dishId());
            item.setDishPrice(itemReq.price());
            item.setDishName(item.getDishName());
            item.setQuantity(itemReq.quantity());
            item.setTotalPrice(itemReq.price().multiply(BigDecimal.valueOf(itemReq.quantity())));
            item.setCreatedAt(LocalDateTime.now());

            cart.getDishes().add(item);
        }

        recalc(cart);

        return toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto addItem(Long cartId, CreateCartItemsRequest request) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found: " + cartId));

        CartItems existing = cart.getDishes().stream()
                .filter(i -> i.getDishId().equals(request.dishId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.quantity());
            existing.setTotalPrice(existing.getDishPrice()
                    .multiply(BigDecimal.valueOf(existing.getQuantity())));
        } else {
            CartItems newItem = new CartItems();
            newItem.setCart(cart);
            newItem.setDishId(request.dishId());
            newItem.setDishPrice(request.price());
            newItem.setDishName(null);
            newItem.setQuantity(request.quantity());
            newItem.setTotalPrice(request.price().multiply(BigDecimal.valueOf(request.quantity())));
            newItem.setCreatedAt(LocalDateTime.now());
            cart.getDishes().add(newItem);
        }

        recalc(cart);

        return toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto updateItem(Long itemId, Integer newQuantity) {
        CartItems item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found: " + itemId));

        Cart cart = item.getCart();

        if (newQuantity <= 0) {
            cart.getDishes().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQuantity);
            item.setTotalPrice(item.getDishPrice().multiply(BigDecimal.valueOf(newQuantity)));
        }

        recalc(cart);
        return toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto deleteItem(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found: " + cartId));

        CartItems item = cart.getDishes().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.getDishes().remove(item);
        cartItemRepository.delete(item);

        recalc(cart);
        return toDto(cartRepository.save(cart));
    }

    @Transactional
    public CartDto clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found: " + cartId));

        cart.getDishes().clear();
        cartItemRepository.deleteAllByCartId(cartId);

        recalc(cart);
        return toDto(cartRepository.save(cart));
    }

    private void recalc(Cart cart) {
        int qty = cart.getDishes().stream()
                .mapToInt(CartItems::getQuantity)
                .sum();

        BigDecimal totalPrice = cart.getDishes().stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setQuantity(qty);
        cart.setTotalPrice(totalPrice);
        cart.setUpdatedAt(LocalDateTime.now());
    }

    private CartDto toDto(Cart cart) {
        return new CartDto(
                cart.getId(),
                cart.getQuantity(),
                cart.getTotalPrice(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getDishes().stream().map(this::toDto).toList()
        );
    }

    private CartItemsDto toDto(CartItems item) {
        return new CartItemsDto(
                item.getId(),
                item.getDishPrice(),
                item.getDishName(),
                item.getTotalPrice(),
                item.getQuantity(),
                item.getCreatedAt()
        );
    }
}