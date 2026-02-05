package com.egrevs.project.domain.repository.restaurant;

import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("Test restaurant getById functionality")
    void givenRestaurant_whenGetById_thenSameRestaurantReturned() {
        // given
        Restaurant restaurant = DataUtils.createRestaurant(RestaurantCuisine.ITALIAN);
        restaurant.setName("Test Resto");
        restaurant = restaurantRepository.save(restaurant);

        // when
        Restaurant found = restaurantRepository.getById(restaurant.getId());

        // then
        assertThat(found.getId()).isEqualTo(restaurant.getId());
        assertThat(found.getName()).isEqualTo("Test Resto");
    }

    @Test
    @DisplayName("Test existsRestaurantByName functionality")
    void givenRestaurant_whenExistsRestaurantByName_thenReturnsTrueForExistingName() {
        // given
        Restaurant restaurant = DataUtils.createRestaurant(RestaurantCuisine.FRENCH);
        restaurant.setName("Fancy Place");
        restaurantRepository.save(restaurant);

        // when
        boolean exists = restaurantRepository.existsRestaurantByName("Fancy Place");
        boolean notExists = restaurantRepository.existsRestaurantByName("Other");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}

