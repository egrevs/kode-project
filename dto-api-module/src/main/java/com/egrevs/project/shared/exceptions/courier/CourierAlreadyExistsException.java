package com.egrevs.project.shared.exceptions.courier;

public class CourierAlreadyExistsException extends RuntimeException {
    public CourierAlreadyExistsException(String message) {
        super(message);
    }
}
