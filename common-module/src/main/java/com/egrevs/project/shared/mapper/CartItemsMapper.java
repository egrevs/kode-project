package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.cart.CartItems;
import com.egrevs.project.shared.dtos.cart.CartItemsDto;

public class CartItemsMapper {

    public static CartItemsDto toDto(CartItems cartItems){
        return new CartItemsDto(
                cartItems.getId(),
                cartItems.getMenuItemPrice(),
                cartItems.getMenuItemName(),
                cartItems.getTotalPrice(),
                cartItems.getQuantity(),
                cartItems.getCreatedAt(),
                cartItems.getMenuItems().getId()
        );
    }
}
