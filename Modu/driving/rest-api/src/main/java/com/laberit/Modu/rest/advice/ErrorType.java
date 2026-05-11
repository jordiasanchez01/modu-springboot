package com.laberit.Modu.rest.advice;

public enum ErrorType {
    VALIDATION_ERROR,
    NOT_FOUND,
    CONFLICT,
    INTERNAL_ERROR
    //TODO: Add other types like Unauthorized, forbidden, if authorization will be finally required.
}
