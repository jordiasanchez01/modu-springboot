package com.laberit.Modu.domain.exceptions;

public class ProductVariantDataIntegrityException extends RuntimeException {
    public ProductVariantDataIntegrityException(Long id) {
        super("ProductVariant with ID (" + id + ")doesn't exist");
    }
}
