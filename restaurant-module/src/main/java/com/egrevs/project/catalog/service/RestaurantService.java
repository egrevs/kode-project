package com.egrevs.project.catalog.service;

import com.egrevs.project.catalog.dto.dish.CreateDishRequest;
import com.egrevs.project.catalog.dto.dish.DishDto;
import com.egrevs.project.catalog.dto.dish.UpdateDishRequest;
import com.egrevs.project.catalog.dto.restaurant.CreateRestaurantRequest;
import com.egrevs.project.catalog.dto.restaurant.FilteredRestaurantRequest;
import com.egrevs.project.catalog.dto.restaurant.RestaurantDto;
import com.egrevs.project.catalog.dto.restaurant.UpdateRestaurantRequest;
import com.egrevs.project.catalog.entity.Dish;
import com.egrevs.project.catalog.entity.Restaurant;
import com.egrevs.project.catalog.exceptions.DishNotFoundException;
import com.egrevs.project.catalog.exceptions.InsufficientRightsToOperateException;
import com.egrevs.project.catalog.exceptions.RestaurantIsAlreadyExistsException;
import com.egrevs.project.catalog.exceptions.RestaurantNotFoundException;
import com.egrevs.project.catalog.repository.DishRepository;
import com.egrevs.project.catalog.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(DishRepository dishRepository, RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    //TODO -> сделать нормальную логику добавления блюд сразу
    @Transactional
    public RestaurantDto createRestaurant(CreateRestaurantRequest request, String userId) {
        if(restaurantRepository.existsRestaurantByName(request.name())){
            throw new RestaurantIsAlreadyExistsException("Restaurant with such name is already exists");
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setUserId(userId);
        restaurant.setName(request.name());
        restaurant.setRating(0.0f);
        restaurant.setCuisine(request.restaurantCuisine());
        restaurant.setDishes(new ArrayList<>());

        var savedRestaurant = restaurantRepository.save(restaurant);

        return toDto(savedRestaurant);
    }

    @Transactional
    public List<RestaurantDto> getFilteredByRatingAndCuisine(FilteredRestaurantRequest request) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        List<RestaurantDto> list = restaurants.stream()
                .filter(r -> r.getRating() == request.rating())
                .filter(r -> r.getCuisine() == request.cuisine())
                .map(this::toDto).toList();

        return list;
    }

    @Transactional
    public RestaurantDto getById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        return toDto(restaurant);
    }

    @Transactional
    public RestaurantDto updateRestaurantById(UpdateRestaurantRequest request, String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        if (request.name() != null) restaurant.setName(request.name());
        if (request.cuisine() != null) restaurant.setCuisine(request.cuisine());
        restaurant.setUpdatedAt(LocalDateTime.now());

        var savedRestaurant = restaurantRepository.save(restaurant);

        return toDto(savedRestaurant);
    }

    @Transactional
    public void closeRestaurantById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        restaurantRepository.delete(restaurant);
    }

    @Transactional
    public DishDto addDish(CreateDishRequest request, String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        Dish dish = new Dish();
        dish.setName(request.name());
        dish.setRestaurant(restaurant);
        dish.setPrice(request.price());
        dish.setIsAvailable(true);
        dish.setCreatedAt(LocalDateTime.now());
        dishRepository.save(dish);

        restaurant.getDishes().add(dish);
        restaurantRepository.save(restaurant);

        return toDto(dish);
    }

    @Transactional
    public List<DishDto> getAllDishesFromRestaurant(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        return restaurant.getDishes()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public DishDto updateDishById(UpdateDishRequest request, String id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        dish.setUpdatedAt(LocalDateTime.now());
        if (request.name() != null) dish.setName(request.name());
        if (request.price() != null) dish.setPrice(request.price());
        dish.setIsAvailable(request.isAvailable());

        var savedDish = dishRepository.save(dish);
        return toDto(savedDish);
    }

    @Transactional
    public void deleteDishById(String id) {
        Dish dish = dishRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        dishRepository.delete(dish);
    }

    //TODO проверки сделать
    @Transactional
    public void changeAvailabilityOfDish(String id, boolean isAvailable) {
        Dish dish = dishRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        if (isAvailable == dish.getIsAvailable()) {
            return;
        }
        dish.setIsAvailable(isAvailable);
        dishRepository.save(dish);
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
                dish.getPrice(),
                dish.getIsAvailable(),
                dish.getCreatedAt(),
                dish.getUpdatedAt()
        );
    }
}
