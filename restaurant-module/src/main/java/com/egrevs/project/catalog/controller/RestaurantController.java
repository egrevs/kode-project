package com.egrevs.project.catalog.controller;

import com.egrevs.project.catalog.service.RestaurantService;
import com.egrevs.project.shared.dtos.menuItem.CreateMenuItemRequest;
import com.egrevs.project.shared.dtos.menuItem.MenuItemsDto;
import com.egrevs.project.shared.dtos.menuItem.UpdateMenuItemRequest;
import com.egrevs.project.shared.dtos.restaurant.CreateRestaurantRequest;
import com.egrevs.project.shared.dtos.restaurant.FilteredRestaurantRequest;
import com.egrevs.project.shared.dtos.restaurant.RestaurantDto;
import com.egrevs.project.shared.dtos.restaurant.UpdateRestaurantRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Operation(summary = "Создать ресторан")
    @PostMapping
    public ResponseEntity<RestaurantDto> createRestaurant(
            @RequestBody CreateRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.createRestaurant(request));
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
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    @Operation(summary = "Обновить ресторан по его ID")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurantById(
            @RequestBody UpdateRestaurantRequest request,
            @PathVariable String id) {
        return ResponseEntity.ok(restaurantService.updateRestaurantById(request, id));
    }

    @Operation(summary = "Удалить ресторан по Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        restaurantService.closeRestaurantById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавить блюдо по Id ресторана")
    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuItemsDto> addDish(@RequestBody CreateMenuItemRequest request,
                                                @PathVariable String id) {
        return ResponseEntity.ok(restaurantService.addDish(request, id));
    }

    @Operation(summary = "Получить все блюда ресторана")
    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemsDto>> findAllDished(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getAllDishesFromRestaurant(id));
    }

    @Operation(summary = "Обновить блюдо по его ID")
    @PutMapping("/menu/{id}")
    public ResponseEntity<MenuItemsDto> updateDishById(
            @PathVariable String id,
            @RequestBody UpdateMenuItemRequest request) {
        return ResponseEntity.ok(restaurantService.updateDishById(request, id));
    }

    @Operation(summary = "Удалить блюдо по его Id")
    @DeleteMapping("menu/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable String id) {
        restaurantService.deleteDishById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновить доступность блюда")
    @PatchMapping("/menu/{id}/availability")
    public ResponseEntity<Void> changeAvailability(@PathVariable String id,
                                                   @RequestParam Boolean isAvailable) {
        restaurantService.changeAvailabilityOfDish(id, isAvailable);
        return ResponseEntity.ok().build();
    }

}
