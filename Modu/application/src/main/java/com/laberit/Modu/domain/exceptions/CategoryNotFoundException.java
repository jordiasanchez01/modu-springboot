package com.laberit.Modu.domain.exceptions;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super("Category: ("+message+") not found");
    }
}
