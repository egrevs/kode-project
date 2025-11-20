package com.egrevs.project.shared.exceptions;

public class ReviewOnNotDeliveredOrderException extends RuntimeException {
    public ReviewOnNotDeliveredOrderException(String message) {
        super("Review can not be created on not delivered order");
    }
}
