package com.egrevs.project.shared.dtos.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderItemsDto(
        String id,
        String dishId,
        String dishName,
        Integer quantity,
        BigDecimal dishPrice,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) {
}
