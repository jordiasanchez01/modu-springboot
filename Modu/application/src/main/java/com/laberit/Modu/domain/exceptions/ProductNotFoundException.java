package com.laberit.Modu.domain.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super("Product: ("+message+") not found");
    }
}
