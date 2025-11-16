package com.egrevs.project.courier.controller;

import com.egrevs.project.courier.service.CourierService;
import com.egrevs.project.shared.dtos.courier.CourierDto;
import com.egrevs.project.shared.dtos.courier.CreateCourierRequest;
import com.egrevs.project.shared.dtos.courier.UpdateCourierStatusRequest;
import com.egrevs.project.shared.dtos.orders.OrderDto;
import com.egrevs.project.shared.exceptions.cartNorders.OrderNotFoundException;
import com.egrevs.project.shared.exceptions.courier.CourierAlreadyExistsException;
import com.egrevs.project.shared.exceptions.courier.CourierNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final CourierService courierService;

    @Operation(summary = "Зарегистрировать нового курьера")
    @PostMapping
    public ResponseEntity<CourierDto> createCourier(@RequestBody CreateCourierRequest request) {
        try {
            return ResponseEntity.ok(courierService.createCourier(request));
        } catch (CourierAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Получить всех курьеров")
    @GetMapping
    public ResponseEntity<List<CourierDto>> getCouriers() {
        return ResponseEntity.ok(courierService.getCouriers());
    }

    @Operation(summary = "Назначить заказ курьеру")
    @PatchMapping("/{id}")
    public ResponseEntity<CourierDto> assignOrderToCourier(@PathVariable String id,
                                                           @RequestParam String orderId) {
        try {
            return ResponseEntity.ok(courierService.assignOrderToCourier(id, orderId));
        } catch (CourierNotFoundException | OrderNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Получить активные заказы курьера")
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDto>> getActiveOrders(@PathVariable String id) {
        try {
            return ResponseEntity.ok(courierService.getActiveCourierOrders(id));
        } catch (CourierNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Изменить статус курьера")
    @PatchMapping("/{id}/status")
    public ResponseEntity<CourierDto> updateStatus(@PathVariable String id,
                                                   @RequestBody UpdateCourierStatusRequest request) {
        try {
            return ResponseEntity.ok(courierService.updateCourierStatus(request, id));
        } catch (CourierNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
