package com.laberit.Modu.ports.driving.command;

public record AddCartItemCommand(
        Long userId,
        Long productVariantId,
        Integer quantity
) {
}
