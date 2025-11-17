package com.egrevs.project.shared.dtos.payments;

import com.egrevs.project.domain.enums.PaymentMethod;
import com.egrevs.project.domain.enums.PaymentStatus;

public record CreatePaymentRequest(
        String orderId,
        PaymentMethod method,
        PaymentStatus status
) {
}
