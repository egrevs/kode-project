package com.egrevs.project.shared.exceptions;

public class OrderNotCancelledException extends RuntimeException {
    public OrderNotCancelledException(String message) {
        super("Order with status DELIVERED can not be cancelled");
    }
}
