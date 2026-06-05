package com.laberit.Modu.rest.advice;


import com.laberit.Modu.domain.exceptions.*;
import com.laberit.Modu.rest.generated.model.ErrorResponse;
import com.laberit.Modu.rest.generated.model.ErrorResponseFieldsInner;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CartEmptyException.class)
    public ErrorResponse handleCartEmpty(CartEmptyException ex) {
        return error(ErrorType.CONFLICT, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CartItemNotFoundException.class)
    public ErrorResponse handleCartItemNotFound(CartItemNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CartNotFoundException.class)
    public ErrorResponse handleCartNotFound(CartNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ErrorResponse handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return error(ErrorType.CONFLICT, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorResponse handleCategoryNotFound(CategoryNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(OrderNotFoundException.class)
    public ErrorResponse handleOrderNotFound(OrderNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(OrderNotPaidException.class)
    public ErrorResponse handleOrderNotPaid(OrderNotPaidException ex) {
        return error(ErrorType.CONFLICT, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(ProductNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ProductVariantNotAvailableException.class)
    public ErrorResponse handleProductVariableNotAvailable(ProductVariantNotAvailableException ex) {
        return error(ErrorType.CONFLICT, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductVariantNotFoundException.class)
    public ErrorResponse handleProductVariableNotFound(ProductVariantNotFoundException ex) {
        return error(ErrorType.NOT_FOUND, ex.getMessage());
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
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
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
