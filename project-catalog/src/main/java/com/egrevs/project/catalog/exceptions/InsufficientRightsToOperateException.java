package com.egrevs.project.catalog.exceptions;

public class InsufficientRightsToOperateException extends RuntimeException {
    public InsufficientRightsToOperateException(String message) {
        super(message);
    }
}
