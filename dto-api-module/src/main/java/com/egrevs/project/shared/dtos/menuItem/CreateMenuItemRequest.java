package com.egrevs.project.shared.dtos.menuItem;

import java.math.BigDecimal;

public record CreateMenuItemRequest(
        String name,
        BigDecimal price
) {
}
