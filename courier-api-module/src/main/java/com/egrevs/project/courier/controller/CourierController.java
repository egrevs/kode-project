package com.egrevs.project.courier.controller;

import com.egrevs.project.courier.service.CourierService;
import com.egrevs.project.shared.dtos.courier.CourierDto;
import com.egrevs.project.shared.dtos.courier.CreateCourierRequest;
import com.egrevs.project.shared.dtos.courier.UpdateCourierStatusRequest;
import com.egrevs.project.shared.dtos.orders.OrderDto;
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
        return ResponseEntity.ok(courierService.createCourier(request));
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
        return ResponseEntity.ok(courierService.assignOrderToCourier(id, orderId));
    }

    @Operation(summary = "Получить активные заказы курьера")
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDto>> getActiveOrders(@PathVariable String id) {
        return ResponseEntity.ok(courierService.getActiveCourierOrders(id));
    }

    @Operation(summary = "Изменить статус курьера")
    @PatchMapping("/{id}/status")
    public ResponseEntity<CourierDto> updateStatus(@PathVariable String id,
                                                   @RequestBody UpdateCourierStatusRequest request) {
        return ResponseEntity.ok(courierService.updateCourierStatus(request, id));
    }
}
