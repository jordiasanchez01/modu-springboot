package com.laberit.Modu.ports.driving.command;

public record AddCartItemCommand(
        Long productVariantId,
        Double price,
        Integer quantity
) {
}
