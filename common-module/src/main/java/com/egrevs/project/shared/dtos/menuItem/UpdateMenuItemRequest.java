package com.egrevs.project.shared.dtos.menuItem;

import java.math.BigDecimal;

public record UpdateMenuItemRequest(
        String name,
        BigDecimal price,
        Boolean isAvailable
) {
}
