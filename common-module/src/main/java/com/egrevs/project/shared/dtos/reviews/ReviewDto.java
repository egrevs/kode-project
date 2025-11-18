package com.egrevs.project.shared.dtos.reviews;

import java.time.LocalDateTime;

public record ReviewDto(
        String id,
        String userId,
        String restaurantId,
        String text,
        Float rating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
