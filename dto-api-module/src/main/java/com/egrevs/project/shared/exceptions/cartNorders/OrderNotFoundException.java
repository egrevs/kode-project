package com.egrevs.project.shared.exceptions.cartNorders;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
