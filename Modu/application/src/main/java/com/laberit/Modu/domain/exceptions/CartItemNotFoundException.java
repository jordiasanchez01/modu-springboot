package com.laberit.Modu.domain.exceptions;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(String message) {
        super("CartItem with ID: "+message+" not found");
    }
}
