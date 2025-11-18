package com.egrevs.project.shared.dtos.payments;

import com.egrevs.project.domain.enums.PaymentMethod;
import com.egrevs.project.domain.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SplitPaymentDto(
        String id,
        String paymentId,
        String restaurantId,
        PaymentStatus status,
        PaymentMethod method,
        BigDecimal price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
