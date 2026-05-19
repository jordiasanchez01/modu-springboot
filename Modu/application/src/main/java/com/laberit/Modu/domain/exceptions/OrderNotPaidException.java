package com.laberit.Modu.domain.exceptions;

public class OrderNotPaidException extends RuntimeException {
    public OrderNotPaidException(String message) {
        super("Payment for Cart with ID: ("+message+") not successful!");
    }
}
