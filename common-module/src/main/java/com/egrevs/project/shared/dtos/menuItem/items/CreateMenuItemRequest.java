package com.egrevs.project.shared.dtos.menuItem.items;

import com.egrevs.project.shared.dtos.menuItem.variants.MenuItemVariantsDto;

import java.math.BigDecimal;
import java.util.List;

public record CreateMenuItemRequest(
        String name,
        BigDecimal price,
        List<MenuItemVariantsDto> variantsList
) {
}
