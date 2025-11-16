package com.egrevs.project.shared.exceptions.cartNorders;

public class OrderIsEmptyException extends RuntimeException {
    public OrderIsEmptyException(String message) {
        super(message);
    }
}
