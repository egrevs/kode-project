package com.egrevs.project.shared.dtos.cart;

import java.math.BigDecimal;

public record CreateCartItemsRequest(
        BigDecimal price,
        Integer quantity,
        String menuItemId
) {
}
