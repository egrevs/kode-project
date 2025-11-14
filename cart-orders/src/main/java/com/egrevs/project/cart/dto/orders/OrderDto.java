package com.egrevs.project.cart.dto.orders;

import java.time.LocalDateTime;

public record OrderDto(
        String userId,
        OrderStatus status,
        String cartId,
        LocalDateTime createdAt
) {
}
