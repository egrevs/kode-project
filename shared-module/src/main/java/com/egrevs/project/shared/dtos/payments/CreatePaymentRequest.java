package com.egrevs.project.shared.dtos.payments;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.enums.PaymentMethod;
import com.egrevs.project.domain.enums.PaymentStatus;

public record CreatePaymentRequest(
        Order order,
        PaymentMethod method,
        PaymentStatus status
) {
}
