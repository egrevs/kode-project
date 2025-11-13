package com.egrevs.project.cart.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartDto(
        Long id,
        Integer quantity,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CartItemsDto> cartItemsDtoList
) {
}
