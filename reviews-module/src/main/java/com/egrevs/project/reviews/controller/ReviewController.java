package com.egrevs.project.reviews.controller;

import com.egrevs.project.reviews.service.ReviewService;
import com.egrevs.project.shared.dtos.reviews.CreateReviewRequest;
import com.egrevs.project.shared.dtos.reviews.ReviewDto;
import com.egrevs.project.shared.dtos.reviews.UpdateReviewRequest;
import com.egrevs.project.shared.exceptions.ReviewNotFoundException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantNotFoundException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        try {
            return ResponseEntity.ok(reviewService.createReview(request));
        } catch (RestaurantNotFoundException | UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Получить все отзывы о ресторане")
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getRestaurantReviews(@RequestParam String restaurantId){
        try {
            return ResponseEntity.ok(reviewService.getRestaurantReviews(restaurantId));
        } catch (RestaurantNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Обновить отзыв")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable String id,
                                                  @RequestBody UpdateReviewRequest request){
        try {
            return ResponseEntity.ok(reviewService.updateReview(request, id));
        } catch (ReviewNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить отзыв")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id){
        try {
            reviewService.deleteReview(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ReviewNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
