package com.egrevs.project.catalog.dto;

import com.egrevs.project.catalog.entity.RestaurantCuisine;

public record FilteredRestaurantRequest(
        float rating,
        RestaurantCuisine cuisine
) {
}
