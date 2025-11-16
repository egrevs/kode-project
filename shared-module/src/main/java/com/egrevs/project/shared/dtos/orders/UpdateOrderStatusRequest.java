package com.egrevs.project.shared.dtos.orders;

import com.egrevs.project.domain.enums.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus status
) {
}
