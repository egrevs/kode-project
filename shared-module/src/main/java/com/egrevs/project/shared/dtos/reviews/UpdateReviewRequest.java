package com.egrevs.project.shared.dtos.reviews;

public record UpdateReviewRequest(
        String text,
        Float rating
) {
}
