package com.egrevs.project.cart.controller;

import com.egrevs.project.cart.dto.cart.CartDto;
import com.egrevs.project.cart.dto.cart.CartItemsDto;
import com.egrevs.project.cart.dto.cart.CreateCartRequest;
import com.egrevs.project.cart.dto.cart.UpdateCartItemsRequest;
import com.egrevs.project.cart.exception.CartNotFoundException;
import com.egrevs.project.cart.service.CartService;
import com.egrevs.project.catalog.exceptions.DishNotFoundException;
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
        try {
            cartService.deleteDishFromCart(id);
            return ResponseEntity.ok("Item us successfully deleted");
        } catch (DishNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Изменить количество блюд в корзине")
    @PutMapping("/items/{id}")
    public ResponseEntity<CartItemsDto> changeItemQuantity(
            @RequestBody UpdateCartItemsRequest request,
            @PathVariable String id){
        try {
            return ResponseEntity.ok(cartService.changeItemQuantity(id, request));
        } catch (DishNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Текущая корзина пользователя")
    @GetMapping
    public ResponseEntity<CartDto> getCart(@RequestParam String userId){
        return ResponseEntity.ok(cartService.getUserCart(userId));
    }

    @Operation(summary = "Очистить корзину")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> clearCart(@PathVariable String id){
        try {
            cartService.clearCart(id);
            return ResponseEntity.ok("Cart is cleared");
        }catch (CartNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
