package com.egrevs.project.shared.dtos.menuItem.items;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        String name,
        BigDecimal price,
        Boolean isAvailable
) {
}
