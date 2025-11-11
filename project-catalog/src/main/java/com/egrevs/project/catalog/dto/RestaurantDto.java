package com.egrevs.project.catalog.dto;

import com.egrevs.project.catalog.entity.RestaurantCuisine;

import java.time.LocalDateTime;
import java.util.List;

public record RestaurantDto(
        Long id,
        Long userId,
        String name,
        float rating,
        RestaurantCuisine cuisine,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<DishDto> dishes
) {
}
