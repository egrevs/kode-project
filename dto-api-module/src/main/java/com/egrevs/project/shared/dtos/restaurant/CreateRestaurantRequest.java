package com.egrevs.project.shared.dtos.restaurant;


import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.shared.dtos.dish.CreateDishRequest;

import java.util.List;

public record CreateRestaurantRequest(
        List<CreateDishRequest> dishRequests,
        String name,
        RestaurantCuisine restaurantCuisine
) {
}
