package com.egrevs.project.shared.exceptions.restaurant;

public class InsufficientRightsToOperateException extends RuntimeException {
    public InsufficientRightsToOperateException(String message) {
        super(message);
    }
}
