package com.laberit.Modu.domain.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super("Product with ID: ("+message+") not found");
    }
}
