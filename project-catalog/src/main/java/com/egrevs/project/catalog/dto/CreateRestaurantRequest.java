package com.egrevs.project.catalog.dto;

import com.egrevs.project.catalog.entity.RestaurantCuisine;

import java.util.List;

public record CreateRestaurantRequest(
        List<CreateDishRequest> dishRequests,
        String name,
        RestaurantCuisine restaurantCuisine
) {
}
