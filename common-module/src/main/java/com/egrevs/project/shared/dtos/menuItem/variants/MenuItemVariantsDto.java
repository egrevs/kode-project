package com.egrevs.project.shared.dtos.menuItem.variants;

import com.egrevs.project.domain.enums.VariantSize;

import java.math.BigDecimal;

public record MenuItemVariantsDto(
        String id,
        VariantSize size,
        BigDecimal price,
        Integer preparationTime
) {
}
