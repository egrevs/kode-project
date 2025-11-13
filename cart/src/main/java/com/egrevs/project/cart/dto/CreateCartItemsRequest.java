package com.egrevs.project.cart.dto;

import java.math.BigDecimal;

public record CreateCartItemsRequest(
        BigDecimal price,
        Integer quantity,
        Long dishId
) {
}
