package com.egrevs.project.shared.dtos.reviews;

public record CreateReviewRequest(
        String userId,
        String restaurantId,
        String text,
        Float rating
) {
}
