package com.egrevs.project.shared.dtos.restaurant;


import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.shared.dtos.menuItem.items.CreateMenuItemRequest;

import java.util.List;

public record CreateRestaurantRequest(
        List<CreateMenuItemRequest> dishRequests,
        String name,
        RestaurantCuisine restaurantCuisine
) {
}
