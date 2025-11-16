package com.egrevs.project.shared.dtos.reviews;

import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.user.User;

public record CreateReviewRequest(
        User user,
        Restaurant restaurant,
        String text,
        Float rating
) {
}
