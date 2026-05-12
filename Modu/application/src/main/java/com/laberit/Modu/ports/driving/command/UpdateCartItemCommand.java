package com.laberit.Modu.ports.driving.command;

public record UpdateCartItemCommand(
        Long cartId,
        Long cartItemId,
        Integer quantity
) {
}
