package com.egrevs.project.cart.dto.cart;

import java.math.BigDecimal;

public record CreateCartItemsRequest(
        BigDecimal price,
        Integer quantity,
        String dishId
) {
}
