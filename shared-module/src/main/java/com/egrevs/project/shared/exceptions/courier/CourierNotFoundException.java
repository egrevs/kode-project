package com.egrevs.project.shared.exceptions.courier;

public class CourierNotFoundException extends RuntimeException {
    public CourierNotFoundException(String message) {
        super("Courier not found: " + message);
    }
}
