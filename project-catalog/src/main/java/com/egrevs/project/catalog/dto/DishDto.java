package com.egrevs.project.catalog.dto;

public record DishDto(
        Long id,
        String name,
        boolean isAvailable
) {
}
