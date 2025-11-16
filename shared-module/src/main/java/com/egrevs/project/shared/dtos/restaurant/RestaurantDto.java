package com.egrevs.project.shared.dtos.restaurant;

import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.shared.dtos.dish.DishDto;

import java.time.LocalDateTime;
import java.util.List;

public record RestaurantDto(
        String id,
        String userId,
        String name,
        float rating,
        RestaurantCuisine cuisine,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<DishDto> dishes
) {
}
