package com.egrevs.project.shared.exceptions.cartNorders;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super(message);
    }
}
