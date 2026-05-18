package com.laberit.Modu.domain.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long userId) {
        super("Cart with ID: ("+userId+") not found");
    }
}
