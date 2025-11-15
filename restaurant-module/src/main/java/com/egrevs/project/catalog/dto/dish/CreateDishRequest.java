package com.egrevs.project.catalog.dto.dish;

import java.math.BigDecimal;

public record CreateDishRequest(
        String name,
        BigDecimal price
) {
}
