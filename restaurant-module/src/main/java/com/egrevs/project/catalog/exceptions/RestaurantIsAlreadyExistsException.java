package com.egrevs.project.catalog.exceptions;

public class RestaurantIsAlreadyExistsException extends RuntimeException {
    public RestaurantIsAlreadyExistsException(String message) {
        super(message);
    }
}
