package com.egrevs.project.catalog.controller;

import com.egrevs.project.catalog.dto.dish.CreateDishRequest;
import com.egrevs.project.catalog.dto.dish.DishDto;
import com.egrevs.project.catalog.dto.dish.UpdateDishRequest;
import com.egrevs.project.catalog.dto.restaurant.CreateRestaurantRequest;
import com.egrevs.project.catalog.dto.restaurant.FilteredRestaurantRequest;
import com.egrevs.project.catalog.dto.restaurant.RestaurantDto;
import com.egrevs.project.catalog.dto.restaurant.UpdateRestaurantRequest;
import com.egrevs.project.catalog.entity.Dish;
import com.egrevs.project.catalog.entity.Restaurant;
import com.egrevs.project.catalog.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "Создать ресторан")
    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @RequestBody CreateRestaurantRequest request,
            @RequestParam Long userId) {
        RestaurantDto restaurantDto = restaurantService.createRestaurant(request, userId);
        return ResponseEntity.ok(restaurantDto);
    }

    @Operation(summary = "Получить отфильтрованный список ресторанов",
            description = "Фильтрация по рейтингу и кухне")
    @GetMapping
    public ResponseEntity<List<RestaurantDto>> getFilteredByRatingAndCuisine(
            @RequestBody FilteredRestaurantRequest request) {
        List<RestaurantDto> restaurantDto = restaurantService.getFilteredByRatingAndCuisine(request);
        return ResponseEntity.ok(restaurantDto);
    }

    @Operation(summary = "Получить ресторан по ID")
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long id) {
        RestaurantDto restaurantDto = restaurantService.getById(id);
        return ResponseEntity.ok(restaurantDto);
    }

    @Operation(summary = "Обновить ресторан по его ID")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurantById(
            @RequestBody UpdateRestaurantRequest request,
            @PathVariable Long id){
        RestaurantDto restaurantDto = restaurantService.updateRestaurantById(request, id);
        return ResponseEntity.ok(restaurantDto);
    }

    @Operation(summary = "Удалить ресторан по Id")
    @DeleteMapping("/{id}")
    public void deleteRestaurant(@PathVariable Long id){
        restaurantService.closeRestaurantById(id);
    }

    @Operation(summary = "Добавить блюдо по Id ресторана")
    @PostMapping("/{id}/menu")
    public ResponseEntity<DishDto> addDish(@RequestBody CreateDishRequest request,
                           @PathVariable Long id){
        DishDto dishDto = restaurantService.addDish(request, id);
        return ResponseEntity.ok(dishDto);
    }

    @Operation(summary = "Получить все блюда ресторана")
    @GetMapping("/{id}/menu")
    public ResponseEntity<List<DishDto>> findAllDished(@PathVariable Long id){
        List<DishDto> dishes = restaurantService.getAllDishesFromRestaurant(id);
        return ResponseEntity.ok(dishes);
    }

    @Operation(summary = "Обновить блюдо по его ID")
    @PutMapping("/menu/{id}")
    public ResponseEntity<DishDto> updateDishById(
            @PathVariable Long id,
            @RequestBody UpdateDishRequest request){
        DishDto updatedDish = restaurantService.updateDishById(request, id);
        return ResponseEntity.ok(updatedDish);
    }

    @Operation(summary = "Удалить блюдо по его Id")
    @DeleteMapping("menu/{id}")
    public ResponseEntity<String> deleteDish(@PathVariable Long id){
        restaurantService.deleteDishById(id);
        return ResponseEntity.ok("Successfully deleted");
    }

    @Operation(summary = "Обновить доступность блюда")
    @PatchMapping("/menu/{id}/availability")
    public void changeAvailability(@PathVariable Long id,
                                   @RequestParam boolean isAvailable){
        restaurantService.changeAvailabilityOfDish(id, isAvailable);
    }

}
