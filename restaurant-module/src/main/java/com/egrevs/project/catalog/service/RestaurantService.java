package com.egrevs.project.catalog.service;

import com.egrevs.project.domain.entity.restaurant.MenuItems;
import com.egrevs.project.domain.entity.restaurant.MenuItemsVariant;
import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.review.Review;
import com.egrevs.project.domain.repository.MenuItemVariantsRepository;
import com.egrevs.project.domain.repository.restaurant.MenuItemsRepository;
import com.egrevs.project.domain.repository.restaurant.RestaurantRepository;
import com.egrevs.project.shared.dtos.menuItem.items.CreateMenuItemRequest;
import com.egrevs.project.shared.dtos.menuItem.items.MenuItemsDto;
import com.egrevs.project.shared.dtos.menuItem.items.UpdateMenuItemRequest;
import com.egrevs.project.shared.dtos.menuItem.variants.MenuItemVariantsDto;
import com.egrevs.project.shared.dtos.restaurant.CreateRestaurantRequest;
import com.egrevs.project.shared.dtos.restaurant.FilteredRestaurantRequest;
import com.egrevs.project.shared.dtos.restaurant.RestaurantDto;
import com.egrevs.project.shared.dtos.restaurant.UpdateRestaurantRequest;
import com.egrevs.project.shared.exceptions.restaurant.DishNotFoundException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantIsAlreadyExistsException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantNotFoundException;
import com.egrevs.project.shared.mapper.CartItemsMapper;
import com.egrevs.project.shared.mapper.ReviewMapper;
import com.egrevs.project.shared.mapper.SplitPaymentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    private final MenuItemsRepository menuItemsRepository;
    private final RestaurantRepository restaurantRepository;
    private MenuItemVariantsRepository menuItemVariantsRepository;

    public RestaurantService(MenuItemsRepository menuItemsRepository, RestaurantRepository restaurantRepository) {
        this.menuItemsRepository = menuItemsRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public RestaurantDto createRestaurant(CreateRestaurantRequest request) {
        if (restaurantRepository.existsRestaurantByName(request.name())) {
            throw new RestaurantIsAlreadyExistsException("Restaurant with such name is already exists");
        }
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.name());
        restaurant.setRating(0.0f);
        restaurant.setCuisine(request.restaurantCuisine());
        restaurant.setMenuItems(new ArrayList<>());

        var savedRestaurant = restaurantRepository.save(restaurant);

        recalculateRating(restaurant);

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

        recalculateRating(restaurant);

        return toDto(savedRestaurant);
    }

    @Transactional
    public void closeRestaurantById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        recalculateRating(restaurant);
        restaurantRepository.delete(restaurant);
    }

    @Transactional
    public MenuItemsDto addDish(CreateMenuItemRequest request, String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));
        MenuItems menuItems = new MenuItems();
        menuItems.setName(request.name());
        menuItems.setRestaurant(restaurant);
        menuItems.setPrice(request.price());
        menuItems.setIsAvailable(true);
        menuItems.setCreatedAt(LocalDateTime.now());

        List<MenuItemsVariant> variants = request.variantsList().stream().map(items -> {
            MenuItemsVariant menuItemsVariant = new MenuItemsVariant();
            menuItemsVariant.setSize(items.size());
            menuItemsVariant.setPrice(items.price());
            menuItemsVariant.setPreparationTime(items.preparationTime());

            return menuItemsVariant;
        }).toList();
        menuItems.setVariants(variants);

        menuItemsRepository.save(menuItems);

        restaurant.getMenuItems().add(menuItems);

        recalculateRating(restaurant);
        restaurantRepository.save(restaurant);

        return toDto(menuItems);
    }

    @Transactional
    public List<MenuItemsDto> getAllDishesFromRestaurant(String id) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() ->
                new RestaurantNotFoundException("No restaurant with id: " + id));

        return restaurant.getMenuItems()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public MenuItemsDto updateDishById(UpdateMenuItemRequest request, String id) {
        MenuItems menuItems = menuItemsRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        menuItems.setUpdatedAt(LocalDateTime.now());
        if (request.name() != null) menuItems.setName(request.name());
        if (request.price() != null) menuItems.setPrice(request.price());
        menuItems.setIsAvailable(request.isAvailable());

        var savedDish = menuItemsRepository.save(menuItems);
        return toDto(savedDish);
    }

    @Transactional
    public void deleteDishById(String id) {
        MenuItems menuItems = menuItemsRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        menuItemsRepository.delete(menuItems);
    }

    //TODO проверки сделать
    @Transactional
    public void changeAvailabilityOfDish(String id, boolean isAvailable) {
        MenuItems menuItems = menuItemsRepository.findById(id).orElseThrow(() ->
                new DishNotFoundException("No dish with id: " + id));

        if (isAvailable == menuItems.getIsAvailable()) {
            return;
        }
        menuItems.setIsAvailable(isAvailable);
        menuItemsRepository.save(menuItems);
    }

    private void recalculateRating(Restaurant restaurant) {
        List<Review> reviews = restaurant.getReviews();
        float rating = (float) reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        if (reviews.isEmpty()) {
            restaurant.setRating(null);
        } else {
            restaurant.setRating(rating);
        }
    }

    private MenuItemVariantsDto toDto(MenuItemsVariant variant) {
        return new MenuItemVariantsDto(
                variant.getId(),
                variant.getSize(),
                variant.getPrice(),
                variant.getPreparationTime()
        );
    }

    private RestaurantDto toDto(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getRating(),
                restaurant.getCuisine(),
                restaurant.getCreatedAt(),
                restaurant.getUpdatedAt(),
                restaurant.getMenuItems().stream().map(this::toDto).collect(Collectors.toList()),
                restaurant.getReviews().stream().map(ReviewMapper::toDto).collect(Collectors.toList()),
                restaurant.getSplitPayment().stream().map(SplitPaymentMapper::toDto).collect(Collectors.toList())
        );
    }

    private MenuItemsDto toDto(MenuItems menuItems) {
        return new MenuItemsDto(
                menuItems.getId(),
                menuItems.getName(),
                menuItems.getPrice(),
                menuItems.getIsAvailable(),
                menuItems.getCreatedAt(),
                menuItems.getUpdatedAt(),
                menuItems.getCartItems().stream().map(CartItemsMapper::toDto).toList(),
                menuItems.getVariants().stream().map(this::toDto).toList()
        );
    }
}
