package com.laberit.Modu.domain.exceptions;

public class ProductVariantNotAvailableException extends RuntimeException {
    public ProductVariantNotAvailableException(Long id, boolean notEnoughStock) {
        super("Product Variant with ID: ("+id+") is not available." + ((notEnoughStock) ? " Not enough stock" : ""));
    }
}
