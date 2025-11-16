package com.egrevs.project.shared.dtos.dish;

import java.math.BigDecimal;

public record UpdateDishRequest(
        String name,
        BigDecimal price,
        Boolean isAvailable
) {
}
