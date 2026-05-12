package com.laberit.Modu.domain.exceptions;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(Long productVariantid) {
        super("There´s not enough stock for product variant with ID ("+productVariantid+")");
    }
}
