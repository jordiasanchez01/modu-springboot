package com.laberit.Modu.ports.driving.command;

public record AddCartItemCommand(
        String deviceId,
        Long productVariantId,
        Integer quantity
) {
}
