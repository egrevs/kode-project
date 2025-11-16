package com.egrevs.project.shared.dtos.restaurant;

import com.egrevs.project.domain.enums.RestaurantCuisine;
import com.egrevs.project.shared.dtos.menuItem.MenuItemDto;
import com.egrevs.project.shared.dtos.reviews.ReviewDto;

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
        List<MenuItemDto> dishes,
        List<ReviewDto> reviews
) {
}
