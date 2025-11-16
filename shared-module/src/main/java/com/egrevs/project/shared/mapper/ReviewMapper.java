package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.review.Review;
import com.egrevs.project.shared.dtos.reviews.ReviewDto;

public class ReviewMapper {

    public static ReviewDto toDto(Review review){
        return new ReviewDto(
                review.getId(),
                review.getUser(),
                review.getRestaurant(),
                review.getText(),
                review.getRating(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
