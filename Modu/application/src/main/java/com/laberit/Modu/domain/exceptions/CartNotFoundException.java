package com.laberit.Modu.domain.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long userId) {
        super("Cart not found for the user with ID: ("+userId+")");
    }
}
