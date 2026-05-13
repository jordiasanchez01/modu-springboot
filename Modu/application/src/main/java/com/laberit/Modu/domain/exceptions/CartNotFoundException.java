package com.laberit.Modu.domain.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String message) {
        super("Cart with ID: ("+message+") not found");
    }
}
