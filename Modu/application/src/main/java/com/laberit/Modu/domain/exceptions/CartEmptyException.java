package com.laberit.Modu.domain.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String deviceId) {
        super("Cart from the user with device ID: ("+deviceId+") is empty.");
    }
}
