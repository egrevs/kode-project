package com.egrevs.project.reviews.controller;

import com.egrevs.project.reviews.service.ReviewService;
import com.egrevs.project.shared.dtos.reviews.CreateReviewRequest;
import com.egrevs.project.shared.dtos.reviews.ReviewDto;
import com.egrevs.project.shared.dtos.reviews.UpdateReviewRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Оставить отзыв ресторану")
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody CreateReviewRequest request){
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @Operation(summary = "Получить все отзывы о ресторане")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getRestaurantReviews(@RequestParam String restaurantId){
        return ResponseEntity.ok(reviewService.getRestaurantReviews(restaurantId));
    }

    @Operation(summary = "Обновить отзыв")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable String id,
                                                  @RequestBody UpdateReviewRequest request){
        return ResponseEntity.ok(reviewService.updateReview(request, id));
    }

    @Operation(summary = "Удалить отзыв")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id){
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}
