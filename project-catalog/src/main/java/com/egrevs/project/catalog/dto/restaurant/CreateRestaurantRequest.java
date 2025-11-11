package com.egrevs.project.catalog.dto.restaurant;

import com.egrevs.project.catalog.dto.dish.CreateDishRequest;
import com.egrevs.project.catalog.entity.RestaurantCuisine;

import java.util.List;

public record CreateRestaurantRequest(
        List<CreateDishRequest> dishRequests,
        String name,
        RestaurantCuisine restaurantCuisine
) {
}
