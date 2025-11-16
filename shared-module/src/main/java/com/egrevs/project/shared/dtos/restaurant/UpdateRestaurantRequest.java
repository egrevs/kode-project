package com.egrevs.project.shared.dtos.restaurant;

import com.egrevs.project.domain.enums.RestaurantCuisine;

public record UpdateRestaurantRequest(
        String name,
        RestaurantCuisine cuisine
) {
}
