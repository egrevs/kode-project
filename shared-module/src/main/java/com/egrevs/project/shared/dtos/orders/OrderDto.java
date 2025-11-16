package com.egrevs.project.shared.dtos.orders;

import com.egrevs.project.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        String id,
        String userId,
        OrderStatus status,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        List<OrderItemsDto> orderItems
) {
}
