package com.egrevs.project.cart.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CartItemsDto(
        Long id,
        BigDecimal dishPrice,
        String dishName,
        BigDecimal totalPrice,
        Integer quantity,
        LocalDateTime createdAt
) {
}
