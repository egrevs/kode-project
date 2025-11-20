package com.egrevs.project.shared.exceptions;

public class InvalidOrderPriceException extends RuntimeException {
    public InvalidOrderPriceException(String message) {
        super("Price must be more than 300.00");
    }
}
