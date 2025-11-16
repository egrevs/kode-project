package com.egrevs.project.shared.dtos.dish;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DishDto(
        String id,
        String name,
        BigDecimal price,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
