package com.egrevs.project.cart.controller;

import com.egrevs.project.cart.dto.orders.CreateOrderRequest;
import com.egrevs.project.cart.dto.orders.OrderDto;
import com.egrevs.project.cart.dto.orders.OrderStatus;
import com.egrevs.project.cart.dto.orders.UpdateOrderStatusRequest;
import com.egrevs.project.cart.exception.CartNotFoundException;
import com.egrevs.project.cart.exception.OrderIsEmptyException;
import com.egrevs.project.cart.exception.OrderNotFoundException;
import com.egrevs.project.cart.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Создать заказ из корзины")
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request){
        try {
            return ResponseEntity.ok(orderService.createOrderWithCart(request));
        } catch (CartNotFoundException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить заказ по ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String id){
        try {
            return ResponseEntity.ok(orderService.getOrder(id));
        }catch (OrderNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Получить все заказы пользователя")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(@RequestParam String userId){
        try {
            return ResponseEntity.ok(orderService.getUserOrders(userId));
        } catch (OrderIsEmptyException e){
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Изменить статус заказа (для ресторана/курьера)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable String id,
            @RequestBody UpdateOrderStatusRequest request){
        try {
            return ResponseEntity.ok(orderService.changeOrderStatus(request, id));
        } catch (OrderNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Отменить заказ")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable String id){
        try {
            orderService.cancelOrder(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OrderNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Отфильтровать заказы по статусу")
    @GetMapping("/status")
    public ResponseEntity<List<OrderDto>> filterByStatus(@RequestParam OrderStatus status){
        return ResponseEntity.ok(orderService.filterByStatus(status));
    }
}
