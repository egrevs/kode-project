package com.egrevs.project.catalog.controller;

import com.egrevs.project.catalog.dto.dish.CreateDishRequest;
import com.egrevs.project.catalog.dto.dish.DishDto;
import com.egrevs.project.catalog.dto.dish.UpdateDishRequest;
import com.egrevs.project.catalog.dto.restaurant.CreateRestaurantRequest;
import com.egrevs.project.catalog.dto.restaurant.FilteredRestaurantRequest;
import com.egrevs.project.catalog.dto.restaurant.RestaurantDto;
import com.egrevs.project.catalog.dto.restaurant.UpdateRestaurantRequest;
import com.egrevs.project.catalog.exceptions.DishNotFoundException;
import com.egrevs.project.catalog.exceptions.RestaurantIsAlreadyExistsException;
import com.egrevs.project.catalog.exceptions.RestaurantNotFoundException;
import com.egrevs.project.catalog.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            @RequestBody CreateRestaurantRequest request,
            @RequestParam String userId) {
        try {
            return ResponseEntity.ok(restaurantService.createRestaurant(request, userId));
        } catch (RestaurantIsAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        }
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
        try {
            return ResponseEntity.ok(restaurantService.getById(id));
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Обновить ресторан по его ID")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDto> updateRestaurantById(
            @RequestBody UpdateRestaurantRequest request,
            @PathVariable String id) {
        try {
            return ResponseEntity.ok(restaurantService.updateRestaurantById(request, id));
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить ресторан по Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable String id) {
        try {
            restaurantService.closeRestaurantById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Добавить блюдо по Id ресторана")
    @PostMapping("/{id}/menu")
    public ResponseEntity<DishDto> addDish(@RequestBody CreateDishRequest request,
                                           @PathVariable String id) {
        try {
            return ResponseEntity.ok(restaurantService.addDish(request, id));
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Получить все блюда ресторана")
    @GetMapping("/{id}/menu")
    public ResponseEntity<List<DishDto>> findAllDished(@PathVariable String id) {
        try {
            return ResponseEntity.ok(restaurantService.getAllDishesFromRestaurant(id));
        } catch (RestaurantNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Обновить блюдо по его ID")
    @PutMapping("/menu/{id}")
    public ResponseEntity<DishDto> updateDishById(
            @PathVariable String id,
            @RequestBody UpdateDishRequest request) {
        try {
            return ResponseEntity.ok(restaurantService.updateDishById(request, id));
        } catch (DishNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить блюдо по его Id")
    @DeleteMapping("menu/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable String id) {
        try {
            restaurantService.deleteDishById(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (DishNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Обновить доступность блюда")
    @PatchMapping("/menu/{id}/availability")
    public ResponseEntity<?> changeAvailability(@PathVariable String id,
                                                @RequestParam Boolean isAvailable) {
        try {
            restaurantService.changeAvailabilityOfDish(id, isAvailable);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (DishNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
