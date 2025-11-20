package com.egrevs.project.shared.exceptions;

public class OrderWithoutItemsException extends RuntimeException {
    public OrderWithoutItemsException(String message) {
        super("Order can not be created without items");
    }
}
