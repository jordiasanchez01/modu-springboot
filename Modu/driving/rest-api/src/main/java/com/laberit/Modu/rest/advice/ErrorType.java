package com.laberit.Modu.rest.advice;

public enum ErrorType {
    VALIDATION_ERROR ("Input validation failed. Check the fields for more details"),
    NOT_FOUND(null),
    CONFLICT(null),
    INTERNAL_ERROR(null);
    //TODO: Add other types like Unauthorized, forbidden, if authorization will be finally required.

    private final String message;


    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
