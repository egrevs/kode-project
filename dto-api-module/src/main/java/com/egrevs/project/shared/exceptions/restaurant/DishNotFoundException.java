package com.egrevs.project.shared.exceptions.restaurant;

public class DishNotFoundException extends RuntimeException {
    public DishNotFoundException(String message) {
        super("Dish not found: " + message);
    }
}
