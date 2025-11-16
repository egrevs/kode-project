package com.egrevs.project.shared.dtos.cart;

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
        List<CartItemsDto> cartItemsDtoList
) {
}
