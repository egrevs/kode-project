package com.egrevs.project.shared.dtos.payments;

import com.egrevs.project.domain.enums.PaymentStatus;

public record UpdatePaymentRequest(
        PaymentStatus status
) {
}
