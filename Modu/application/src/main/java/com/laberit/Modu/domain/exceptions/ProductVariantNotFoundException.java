package com.laberit.Modu.domain.exceptions;

public class ProductVariantNotFoundException extends RuntimeException {
    public ProductVariantNotFoundException(Long id) {
        super("Product Variant with ID: ("+id+") not found");
    }
}
