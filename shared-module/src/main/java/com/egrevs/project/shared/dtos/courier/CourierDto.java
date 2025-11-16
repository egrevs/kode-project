package com.egrevs.project.shared.dtos.courier;

import com.egrevs.project.domain.enums.CourierStatus;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.shared.dtos.orders.OrderDto;

import java.time.LocalDateTime;
import java.util.List;

public record CourierDto(
        String id,
        String name,
        String login,
        String email,
        String password,
        UserRole userRole,
        CourierStatus status,
        LocalDateTime createdAt,
        List<OrderDto> orderList
) {
}
