package com.egrevs.project.shared.dtos.reviews;

import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.user.User;

import java.time.LocalDateTime;

public record ReviewDto(
        String id,
        User user,
        Restaurant restaurant,
        String text,
        Float rating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
