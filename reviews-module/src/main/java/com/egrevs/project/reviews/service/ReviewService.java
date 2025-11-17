package com.egrevs.project.reviews.service;

import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.review.Review;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.repository.ReviewRepository;
import com.egrevs.project.domain.repository.UserRepository;
import com.egrevs.project.domain.repository.restaurant.RestaurantRepository;
import com.egrevs.project.shared.dtos.reviews.CreateReviewRequest;
import com.egrevs.project.shared.dtos.reviews.ReviewDto;
import com.egrevs.project.shared.dtos.reviews.UpdateReviewRequest;
import com.egrevs.project.shared.exceptions.ReviewNotFoundException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantNotFoundException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import com.egrevs.project.shared.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    //TODO сделать изменение рейтинга у ресторана
    @Transactional
    public ReviewDto createReview(CreateReviewRequest request){
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId()).orElseThrow(() ->
                new RestaurantNotFoundException("Restaurant with id " + request.restaurantId() + " doesn't exists"));

        User user = userRepository.findById(request.userId()).orElseThrow(
                () -> new UserNotFoundException("User with id " + request.userId() + " not found"));

        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setText(request.text());
        review.setRating(request.rating());
        review.setCreatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        return ReviewMapper.toDto(savedReview);
    }

    @Transactional
    public List<ReviewDto> getRestaurantReviews(String restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() ->
                new RestaurantNotFoundException("Restaurant with id " + restaurantId + " doesn't exists"));

        return restaurant.getReviews()
                .stream()
                .map(ReviewMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDto updateReview(UpdateReviewRequest request, String id){
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new ReviewNotFoundException("Review with id " + id + " doesn't exists"));

        if(request.text() != null) review.setText(request.text());
        if(request.rating() != null) review.setText(request.text());
        review.setUpdatedAt(LocalDateTime.now());

        Review savedReview = reviewRepository.save(review);

        return ReviewMapper.toDto(savedReview);
    }

    @Transactional
    public void deleteReview(String id){
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new ReviewNotFoundException("Review with id " + id + " doesn't exists"));

        reviewRepository.delete(review);
    }
}
