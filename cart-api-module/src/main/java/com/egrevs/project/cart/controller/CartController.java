package com.egrevs.project.cart.controller;

import com.egrevs.project.cart.service.CartService;
import com.egrevs.project.shared.dtos.cart.CartDto;
import com.egrevs.project.shared.dtos.cart.CartItemsDto;
import com.egrevs.project.shared.dtos.cart.CreateCartRequest;
import com.egrevs.project.shared.dtos.cart.UpdateCartItemsRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Добавить блюдо в корзину")
    @PostMapping("/items")
    public ResponseEntity<CartDto> createCart(
            @RequestBody CreateCartRequest request,
            @RequestParam String userId){
        return ResponseEntity.ok(cartService.createCart(request, userId));
    }

    @Operation(summary = "Удалить блюдо")
    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> deleteCartItem(@PathVariable String id){
        cartService.deleteDishFromCart(id);
        return ResponseEntity.ok("Item is successfully deleted");
    }

    @Operation(summary = "Изменить количество блюд в корзине")
    @PutMapping("/items/{id}")
    public ResponseEntity<CartItemsDto> changeItemQuantity(
            @RequestBody UpdateCartItemsRequest request,
            @PathVariable String id){
        return ResponseEntity.ok(cartService.changeItemQuantity(id, request));
    }

    @Operation(summary = "Текущая корзина пользователя")
    @GetMapping
    public ResponseEntity<CartDto> getCart(@RequestParam String userId){
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    @Operation(summary = "Очистить корзину")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> clearCart(@PathVariable String id){
        cartService.clearCart(id);
        return ResponseEntity.ok("Cart is cleared");
    }

}
