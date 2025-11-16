package com.egrevs.project.shared.dtos.menuItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MenuItemDto(
        String id,
        String name,
        BigDecimal price,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
