package com.laberit.Modu.ports.driving.command;

import com.laberit.Modu.domain.model.CartItem;

import java.time.LocalDateTime;
import java.util.List;

public record AddCartCommand(
        Long userId,
        LocalDateTime createdAt,
        List<CartItem> cartItems
) {
}
