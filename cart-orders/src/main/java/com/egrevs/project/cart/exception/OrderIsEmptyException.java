package com.egrevs.project.cart.exception;

public class OrderIsEmptyException extends RuntimeException {
    public OrderIsEmptyException(String message) {
        super(message);
    }
}
