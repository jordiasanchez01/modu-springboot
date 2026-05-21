package com.laberit.Modu.domain.exceptions;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(String deviceId) {
        super("Cart not found for the user with device ID: ("+deviceId+")");
    }
}
