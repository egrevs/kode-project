package com.egrevs.project.catalog.dto.dish;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DishDto(
        Long id,
        String name,
        BigDecimal price,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
