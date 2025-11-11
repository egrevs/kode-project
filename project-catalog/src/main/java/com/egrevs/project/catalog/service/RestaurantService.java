package com.egrevs.project.catalog.service;

import com.egrevs.project.catalog.dto.CreateRestaurantRequest;
import com.egrevs.project.catalog.dto.DishDto;
import com.egrevs.project.catalog.dto.RestaurantDto;
import com.egrevs.project.catalog.entity.Dish;
import com.egrevs.project.catalog.entity.Restaurant;
import com.egrevs.project.catalog.entity.RestaurantCuisine;
import com.egrevs.project.catalog.exceptions.InsufficientRightsToOperateException;
import com.egrevs.project.catalog.repository.DishRepository;
import com.egrevs.project.catalog.repository.RestaurantRepository;
import com.egrevs.project.gateway.entity.User;
import com.egrevs.project.gateway.entity.UserRole;
import com.egrevs.project.gateway.exceptions.UserNotFoundException;
import com.egrevs.project.gateway.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(DishRepository dishRepository, RestaurantRepository restaurantRepository,
                             UserRepository repository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = repository;
    }

    //TODO -> сделать нормальную логику добавления блюд сразу
    public RestaurantDto createRestaurant(CreateRestaurantRequest request, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        validateUserRoleForRestaurantCreation(user);

        Restaurant restaurant = new Restaurant();
        restaurant.setUserId(user.getId());
        restaurant.setName(request.name());
        restaurant.setRating(0.0f);
        restaurant.setCuisine(request.restaurantCuisine());
        restaurant.setDishes(new ArrayList<>());

        var savedRestaurant = restaurantRepository.save(restaurant);

        return toDto(savedRestaurant);
    }

    public List<RestaurantDto> getFilteredByRatingAndCuisine(float rating, RestaurantCuisine cuisine){
        
    }

    public static void validateUserRoleForRestaurantCreation(User user) {
        if (user.getRole() == UserRole.COURIER || user.getRole() == UserRole.USER) {
            throw new InsufficientRightsToOperateException("User role is invalid");
        }
    }

    private RestaurantDto toDto(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getUserId(),
                restaurant.getName(),
                restaurant.getRating(),
                restaurant.getCuisine(),
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt(),
                restaurant.getDishes().stream().map(this::toDto).collect(Collectors.toList())
        );
    }

    private DishDto toDto(Dish dish) {
        return new DishDto(
                dish.getId(),
                dish.getName(),
                dish.isAvailable()
        );
    }
}
