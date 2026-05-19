package com.laberit.Modu.domain.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super("Order with ID: ("+message+") not found");
    }
}
