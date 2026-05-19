package com.laberit.Modu.ports.driving.command;

public record AddOrderItemCommand(
        Long userId,
        Long productVariantId,
        Integer quantity
) {
}
