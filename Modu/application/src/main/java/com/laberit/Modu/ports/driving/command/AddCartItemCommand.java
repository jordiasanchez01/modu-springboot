package com.laberit.Modu.ports.driving.command;

public record AddCartItemCommand(
        Long cartId,
        Long productVariantId,
        Integer quantity
) {
}
