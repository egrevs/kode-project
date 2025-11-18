package com.egrevs.project.shared.exceptions;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String message) {
        super("Notification not found: " + message);
    }
}
