package com.egrevs.project.catalog.dto;

import com.egrevs.project.catalog.entity.RestaurantCuisine;

public record UpdateRestaurantRequest(
        String name,
        RestaurantCuisine cuisine
) {
}
