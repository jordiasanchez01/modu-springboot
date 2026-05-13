package com.laberit.Modu.domain.exceptions;

public class ProductVariantNotAvailableException extends RuntimeException {
    public ProductVariantNotAvailableException(String message) {
        super("Product Variant with ID: ("+message+") not found");
    }
}
