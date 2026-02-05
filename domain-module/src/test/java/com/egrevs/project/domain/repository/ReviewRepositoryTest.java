package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.review.Review;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.util.DataUtils;
import com.egrevs.project.domain.repository.restaurant.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("Test review save and findById functionality")
    void givenReviewForRestaurantAndUser_whenSaveAndFindById_thenReviewReturned() {
        // given
        User user = DataUtils.createUser(UserRole.USER);
        userRepository.save(user);

        Restaurant restaurant = DataUtils.createRestaurant(RestaurantCuisine.ITALIAN);
        restaurantRepository.save(restaurant);

        Review review = DataUtils.createReview(user, restaurant);
        Review saved = reviewRepository.save(review);

        // when
        Review found = reviewRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getUser().getId()).isEqualTo(user.getId());
        assertThat(found.getRestaurant().getId()).isEqualTo(restaurant.getId());
    }
}

