package com.egrevs.project.shared.dtos.cart;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CartItemsDto(
        String id,
        BigDecimal menuItemPrice,
        String menuItemName,
        BigDecimal totalPrice,
        Integer quantity,
        LocalDateTime createdAt
) {
}
