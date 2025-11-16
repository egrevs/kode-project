package com.egrevs.project.shared.dtos.dish;

import java.math.BigDecimal;

public record CreateDishRequest(
        String name,
        BigDecimal price
) {
}
