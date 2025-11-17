package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.review.Review;
import com.egrevs.project.shared.dtos.reviews.ReviewDto;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public static ReviewDto toDto(Review review){
        return new ReviewDto(
                review.getId(),
                review.getUser().getId(),
                review.getRestaurant().getId(),
                review.getText(),
                review.getRating(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
