package com.laberit.Modu.domain.exceptions;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super("The user´s cart is empty.");
    }
}
