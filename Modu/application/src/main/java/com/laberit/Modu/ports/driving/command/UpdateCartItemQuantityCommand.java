package com.laberit.Modu.ports.driving.command;

public record UpdateCartItemQuantityCommand(
        Long cartItemId,
        Integer quantity
) {
}
