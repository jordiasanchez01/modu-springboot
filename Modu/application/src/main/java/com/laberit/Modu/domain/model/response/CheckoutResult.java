package com.laberit.Modu.domain.model.response;

import com.laberit.Modu.domain.model.Order;
import lombok.Builder;

@Builder
public record CheckoutResult(
        Order order,
        CartWithAllChecks cartResponse
) {
    public boolean isOrderPlaced() {
        return cartResponse.changedPrices().isEmpty() &&
            cartResponse.insufficientStock().isEmpty() &&
            cartResponse.unavailableVariants().isEmpty();
    }
}
