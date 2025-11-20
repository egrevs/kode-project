package com.egrevs.project.shared.exceptions;

public class OrderIsNotReadyException extends RuntimeException {
    public OrderIsNotReadyException(String message) {
        super("Order must be ready to be grabbed by courier");
    }
}
