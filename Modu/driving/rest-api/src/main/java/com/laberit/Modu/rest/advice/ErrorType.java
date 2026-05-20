package com.laberit.Modu.rest.advice;

public enum ErrorType {
    VALIDATION_ERROR ("Input validation failed. Check the fields for more details"),
    AUTHENTIFICATION_NEEDED ("User´s device ID missing in the Authorization header."),
    NOT_FOUND(null),
    CONFLICT(null),
    INTERNAL_ERROR(null);

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
