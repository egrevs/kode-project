package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.payment.SplitPayment;
import com.egrevs.project.shared.dtos.payments.SplitPaymentDto;

public class SplitPaymentMapper {

    public static SplitPaymentDto toDto(SplitPayment splitPayment){
        return new SplitPaymentDto(
                splitPayment.getId(),
                splitPayment.getPayment().getId(),
                splitPayment.getRestaurant().getId(),
                splitPayment.getStatus(),
                splitPayment.getMethod(),
                splitPayment.getPrice(),
                splitPayment.getCreatedAt(),
                splitPayment.getUpdatedAt()
        );
    }
}
