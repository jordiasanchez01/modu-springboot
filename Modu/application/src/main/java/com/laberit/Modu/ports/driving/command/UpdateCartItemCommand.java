package com.laberit.Modu.ports.driving.command;

public record UpdateCartItemCommand(
        Long productVariantId,
        Double price,
        Integer quantity
) {
}
