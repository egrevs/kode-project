package com.egrevs.project.shared.exceptions;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super("Review not found: " + message);
    }
}
