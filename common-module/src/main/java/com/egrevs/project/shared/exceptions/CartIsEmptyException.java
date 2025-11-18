package com.egrevs.project.shared.exceptions;

public class CartIsEmptyException extends RuntimeException {
    public CartIsEmptyException(String message) {
        super("Cart is empty: " + message);
    }
}
