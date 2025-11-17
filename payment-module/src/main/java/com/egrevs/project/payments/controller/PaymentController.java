package com.egrevs.project.payments.controller;

import com.egrevs.project.domain.enums.PaymentStatus;
import com.egrevs.project.payments.service.PaymentService;
import com.egrevs.project.shared.dtos.payments.CreatePaymentRequest;
import com.egrevs.project.shared.dtos.payments.PaymentDto;
import com.egrevs.project.shared.dtos.payments.UpdatePaymentRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Создать новый платеж")
    @PostMapping
    public ResponseEntity<PaymentDto> createPayment(@RequestBody CreatePaymentRequest request){
        return ResponseEntity.ok(paymentService.createPayment(request));
    }

    @Operation(summary = "Получить статус платежа")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentStatus> getPaymentStatus(@PathVariable String id){
        return ResponseEntity.ok(paymentService.getPaymentStatus(id));
    }

    @Operation(summary = "Обновить статус платежа")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentDto> updatePaymentStatus(@RequestBody UpdatePaymentRequest request,
                                                          @PathVariable String id){
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, request));
    }

    @Operation(summary = "Получить платеж по ID заказа")
    @GetMapping
    public ResponseEntity<List<PaymentDto>> getPaymentByOrder(@RequestParam String id){
        return ResponseEntity.ok(paymentService.getPaymentByOrder(id));
    }
}
