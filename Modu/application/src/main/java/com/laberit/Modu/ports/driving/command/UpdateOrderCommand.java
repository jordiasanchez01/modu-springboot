package com.laberit.Modu.ports.driving.command;

import com.laberit.Modu.domain.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateOrderCommand(
        Long userId,
        LocalDateTime createdAt,
        List<OrderItem> orderItems
) {
}
