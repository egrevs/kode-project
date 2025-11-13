package com.egrevs.project.cart.service;

import com.egrevs.project.cart.repository.CartItemsRepository;
import com.egrevs.project.cart.repository.CartsRepository;
import com.egrevs.project.catalog.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartsRepository cartRepository;
    private final CartItemsRepository cartItemRepository;
    private final RestaurantService restaurantService;


}