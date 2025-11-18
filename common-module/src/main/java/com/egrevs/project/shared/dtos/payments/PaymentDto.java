package com.egrevs.project.shared.dtos.payments;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.enums.PaymentMethod;
import com.egrevs.project.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        String id,
        Order order,
        BigDecimal totalAmount,
        PaymentStatus status,
        PaymentMethod method,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
