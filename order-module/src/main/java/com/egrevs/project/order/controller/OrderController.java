
package com.egrevs.project.order.controller;

import com.egrevs.project.domain.enums.OrderStatus;
import com.egrevs.project.order.service.OrderService;
import com.egrevs.project.shared.dtos.orders.CreateOrderRequest;
import com.egrevs.project.shared.dtos.orders.OrderDto;
import com.egrevs.project.shared.dtos.orders.UpdateOrderStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrderWithCart(request));
    }

    @Operation(summary = "Получить заказ по ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @Operation(summary = "Получить все заказы пользователя")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(@RequestParam String userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @Operation(summary = "Изменить статус заказа (для ресторана/курьера)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> changeOrderStatus(
            @PathVariable String id,
            @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.changeOrderStatus(request, id));
    }

    @Operation(summary = "Отменить заказ")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable String id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Отфильтровать заказы по статусу")
    @GetMapping("/status")
    public ResponseEntity<List<OrderDto>> filterByStatus(@RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.filterByStatus(status));
    }
}
