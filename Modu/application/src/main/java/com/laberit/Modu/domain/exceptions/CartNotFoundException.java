package com.laberit.Modu.domain.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException() {
        super("Cart not found for the authenticated user.");
    }
}
