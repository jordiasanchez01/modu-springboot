package com.laberit.Modu.ports.driving.command;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record AddOrderCommand(
    Boolean isPaid,
    String specialInstructions
) {
}
