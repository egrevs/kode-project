package com.egrevs.project.catalog.service;

import com.egrevs.project.domain.entity.restaurant.MenuItem;
import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.repository.restaurant.MenuItemRepository;
import com.egrevs.project.domain.repository.restaurant.RestaurantRepository;
import com.egrevs.project.shared.dtos.menuItem.CreateMenuItemRequest;
import com.egrevs.project.shared.dtos.menuItem.MenuItemDto;
import com.egrevs.project.shared.dtos.menuItem.UpdateMenuItemRequest;
import com.egrevs.project.shared.dtos.restaurant.CreateRestaurantRequest;
import com.egrevs.project.shared.dtos.restaurant.FilteredRestaurantRequest;
import com.egrevs.project.shared.dtos.restaurant.RestaurantDto;
import com.egrevs.project.shared.dtos.restaurant.UpdateRestaurantRequest;
import com.egrevs.project.shared.exceptions.restaurant.DishNotFoundException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantIsAlreadyExistsException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    public RestaurantService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
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
        restaurant.setMenuItems(new ArrayList<>());

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
    public MenuItemDto addDish(CreateMenuItemRequest request, String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        MenuItem menuItem = new MenuItem();
        menuItem.setName(request.name());
        menuItem.setRestaurant(restaurant);
        menuItem.setPrice(request.price());
        menuItem.setIsAvailable(true);
        menuItem.setCreatedAt(LocalDateTime.now());
        menuItemRepository.save(menuItem);

        restaurant.getMenuItems().add(menuItem);
        restaurantRepository.save(restaurant);

        return toDto(menuItem);
    }

    @Transactional
    public List<MenuItemDto> getAllDishesFromRestaurant(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        return restaurant.getMenuItems()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public MenuItemDto updateDishById(UpdateMenuItemRequest request, String id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        menuItem.setUpdatedAt(LocalDateTime.now());
        if (request.name() != null) menuItem.setName(request.name());
        if (request.price() != null) menuItem.setPrice(request.price());
        menuItem.setIsAvailable(request.isAvailable());

        var savedDish = menuItemRepository.save(menuItem);
        return toDto(savedDish);
    }

    @Transactional
    public void deleteDishById(String id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        menuItemRepository.delete(menuItem);
    }

    //TODO проверки сделать
    @Transactional
    public void changeAvailabilityOfDish(String id, boolean isAvailable) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        if (isAvailable == menuItem.getIsAvailable()) {
            return;
        }
        menuItem.setIsAvailable(isAvailable);
        menuItemRepository.save(menuItem);
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
                restaurant.getMenuItems().stream().map(this::toDto).collect(Collectors.toList())
        );
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        return new MenuItemDto(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getPrice(),
                menuItem.getIsAvailable(),
                menuItem.getCreatedAt(),
                menuItem.getUpdatedAt()
        );
    }
}
