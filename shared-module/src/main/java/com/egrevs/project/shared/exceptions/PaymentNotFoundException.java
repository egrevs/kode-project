package com.egrevs.project.shared.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super("Payment not found: " + message);
    }
}
