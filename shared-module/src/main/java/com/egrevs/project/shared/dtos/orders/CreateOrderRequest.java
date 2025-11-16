package com.egrevs.project.shared.dtos.orders;

import com.egrevs.project.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        String userId,
        String cartId,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderItemsDto> orderItemslist
) {
}
