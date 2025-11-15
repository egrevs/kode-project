package com.egrevs.project.cart.dto.orders;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        String userId,
        String cartId,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderItemsDto> orderItemslist
) {
}
