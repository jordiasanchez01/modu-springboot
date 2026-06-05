package com.laberit.Modu.domain.exceptions;

public class ProductVariantNotFoundException extends RuntimeException {
    public ProductVariantNotFoundException(String message) {
        super("Product Variant with ID: ("+message+") not found");
    }
}
