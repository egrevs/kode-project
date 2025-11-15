package com.egrevs.project.catalog.dto.restaurant;

import com.egrevs.project.catalog.entity.RestaurantCuisine;

public record UpdateRestaurantRequest(
        String name,
        RestaurantCuisine cuisine
) {
}
