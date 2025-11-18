package com.egrevs.project.shared.dtos.menuItem;

import com.egrevs.project.shared.dtos.cart.CartItemsDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MenuItemsDto(
        String id,
        String name,
        BigDecimal price,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CartItemsDto> cartItemsDtoList
) {
}
