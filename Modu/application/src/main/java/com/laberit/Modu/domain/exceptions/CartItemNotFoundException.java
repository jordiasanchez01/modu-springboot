package com.laberit.Modu.domain.exceptions;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long  id) {
        super("CartItem with ID: "+id+" not found");
    }
}
