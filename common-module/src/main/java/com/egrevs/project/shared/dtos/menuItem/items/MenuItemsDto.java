package com.egrevs.project.shared.dtos.menuItem.items;

import com.egrevs.project.shared.dtos.cart.CartItemsDto;
import com.egrevs.project.shared.dtos.menuItem.variants.MenuItemVariantsDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MenuItemsDto(
        String id,
        String name,
        BigDecimal price,
        Boolean isAvailable,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CartItemsDto> cartItemsDtoList,
        List<MenuItemVariantsDto> variantsDtos
) {
}
