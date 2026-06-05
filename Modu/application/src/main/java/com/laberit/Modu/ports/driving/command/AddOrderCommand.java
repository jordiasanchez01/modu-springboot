package com.laberit.Modu.ports.driving.command;

import com.laberit.Modu.domain.model.CartItemsQuantitiesUpdateDTO;

public record AddOrderCommand(
    Boolean isPaid,
    String specialInstructions,
    double shippingCosts,
    CartItemsQuantitiesUpdateDTO cartToOrder
) {
}
