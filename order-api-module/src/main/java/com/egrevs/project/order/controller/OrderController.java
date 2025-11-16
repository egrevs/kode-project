
package com.egrevs.project.order.controller;

import com.egrevs.project.domain.enums.OrderStatus;
import com.egrevs.project.order.service.OrderService;
import com.egrevs.project.shared.dtos.orders.CreateOrderRequest;
import com.egrevs.project.shared.dtos.orders.OrderDto;
import com.egrevs.project.shared.dtos.orders.UpdateOrderStatusRequest;
import com.egrevs.project.shared.exceptions.cartNorders.CartNotFoundException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderIsEmptyException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderNotFoundException;
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
