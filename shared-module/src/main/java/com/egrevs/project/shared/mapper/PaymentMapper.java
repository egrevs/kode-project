package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.payment.Payment;
import com.egrevs.project.shared.dtos.payments.PaymentDto;

public class PaymentMapper {

    public static PaymentDto toDto(Payment payment){
        return new PaymentDto(
                payment.getId(),
                payment.getOrder(),
                payment.getTotalAmount(),
                payment.getPaymentStatus(),
                payment.getMethod(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
