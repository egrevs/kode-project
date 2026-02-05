package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.restaurant.MenuItems;
import com.egrevs.project.domain.entity.restaurant.MenuItemsVariant;
import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.domain.enums.VariantSize;
import com.egrevs.project.domain.repository.restaurant.MenuItemsRepository;
import com.egrevs.project.domain.repository.restaurant.RestaurantRepository;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MenuItemVariantsRepositoryTest {

    @Autowired
    private MenuItemVariantsRepository menuItemVariantsRepository;

    @Autowired
    private MenuItemsRepository menuItemsRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("Test menu item variant save and findById functionality")
    void givenMenuItemVariantForMenuItem_whenSaveAndFindById_thenVariantReturned() {
        // given
        Restaurant restaurant = DataUtils.createRestaurant(RestaurantCuisine.CHINESE);
        restaurantRepository.save(restaurant);

        MenuItems menuItem = DataUtils.createMenuItem(restaurant);
        menuItem.setName("Noodles");
        menuItem = menuItemsRepository.save(menuItem);

        MenuItemsVariant variant = DataUtils.createMenuItemVariant(menuItem, VariantSize.MEDIUM);

        MenuItemsVariant saved = menuItemVariantsRepository.save(variant);

        // when
        MenuItemsVariant found = menuItemVariantsRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getMenuItems().getId()).isEqualTo(menuItem.getId());
        assertThat(found.getSize()).isEqualTo(VariantSize.MEDIUM);
    }
}

