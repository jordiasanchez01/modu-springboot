package com.laberit.Modu.rest.advice;


import com.laberit.Modu.domain.exceptions.*;
import com.laberit.Modu.rest.generated.model.ErrorResponse;
import com.laberit.Modu.rest.generated.model.ErrorResponseFieldsInner;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CartNotFoundException.class)
    public ErrorResponse handleCartNotFound(CartNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(ProductNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductVariantNotFoundException.class)
    public ErrorResponse handleProductVariantNotFound(ProductVariantNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorResponse handleCategoryNotFound(CategoryNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotEnoughStockException.class)
    public ErrorResponse handleNotEnoughStock(NotEnoughStockException ex) {
        return error(ErrorType.CONFLICT, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorResponseFieldsInner> responseFields = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                        new ErrorResponseFieldsInner(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        return error(ErrorType.VALIDATION_ERROR, ErrorType.VALIDATION_ERROR.getMessage(), responseFields);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        List<ErrorResponseFieldsInner> responseFields = ex.getConstraintViolations().stream()
                .map(constraintViolation ->
                        new ErrorResponseFieldsInner(constraintViolation.getPropertyPath().toString().split("\\.")[1], constraintViolation.getMessage()))
                .toList();
        return error(ErrorType.VALIDATION_ERROR, ErrorType.VALIDATION_ERROR.getMessage(), responseFields);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        List<ErrorResponseFieldsInner> responseFields = List.of(new ErrorResponseFieldsInner(ex.getName(), ex.getMessage()));
        return error(ErrorType.VALIDATION_ERROR, ErrorType.VALIDATION_ERROR.getMessage(), responseFields);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGeneric(Exception ex) {
        return error(ErrorType.INTERNAL_ERROR, ex.getMessage());
    }

    private ErrorResponse error(ErrorType type, String message) {
        return new ErrorResponse()
                .type(type.name())
                .message(message)
                .fields(null);
    }

    private ErrorResponse error(ErrorType type, String message, List<ErrorResponseFieldsInner> fields) {
        return new ErrorResponse()
                .type(type.name())
                .message(message)
                .fields(fields);
    }
}
