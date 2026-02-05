package com.egrevs.project.domain.repository.restaurant;

import com.egrevs.project.domain.entity.restaurant.MenuItems;
import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MenuItemsRepositoryTest {

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("Test menu item save and findById functionality")
    void givenMenuItemForRestaurant_whenSaveAndFindById_thenMenuItemReturned() {
        // given
        Restaurant restaurant = DataUtils.createRestaurant(RestaurantCuisine.SPANISH);
        restaurantRepository.save(restaurant);

        MenuItems menuItem = DataUtils.createMenuItem(restaurant);
        menuItem.setName("Paella");

        MenuItems saved = menuItemsRepository.save(menuItem);

        // when
        MenuItems found = menuItemsRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(found.getName()).isEqualTo("Paella");
    }
}

