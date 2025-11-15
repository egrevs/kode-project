package com.egrevs.project.catalog.dto.restaurant;

import com.egrevs.project.catalog.dto.dish.DishDto;
import com.egrevs.project.catalog.entity.RestaurantCuisine;

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
