package com.egrevs.project.cart.dto.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CartDto(
        String id,
        String userId,
        Integer quantity,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CartItemDto> cartItemsDtoList
) {
}
