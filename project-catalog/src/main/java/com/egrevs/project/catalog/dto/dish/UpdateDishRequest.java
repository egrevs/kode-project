package com.egrevs.project.catalog.dto.dish;

import java.math.BigDecimal;

public record UpdateDishRequest(
        String name,
        BigDecimal price,
        boolean isAvailable
) {
}
