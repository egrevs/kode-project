package com.egrevs.project.cart.dto.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CartItemsDto(
        String id,
        BigDecimal dishPrice,
        String dishName,
        BigDecimal totalPrice,
        Integer quantity,
        LocalDateTime createdAt
) {
}
