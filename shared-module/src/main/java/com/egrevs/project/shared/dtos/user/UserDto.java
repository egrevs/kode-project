package com.egrevs.project.shared.dtos.user;

import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.shared.dtos.orders.OrderDto;
import com.egrevs.project.shared.dtos.reviews.ReviewDto;

import java.time.LocalDateTime;
import java.util.List;

public record UserDto(
        String id,
        String name,
        String email,
        String login,
        UserRole role,
        LocalDateTime created_at,
        LocalDateTime updated_at,
        List<ReviewDto> reviewList,
        List<OrderDto> orderList,
        String cartId
) {
}
