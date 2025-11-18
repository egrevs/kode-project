package com.egrevs.project.shared.exceptions.restaurant;

public class RestaurantIsAlreadyExistsException extends RuntimeException {
    public RestaurantIsAlreadyExistsException(String message) {
        super("Restaurant already exists: " + message);
    }
}
