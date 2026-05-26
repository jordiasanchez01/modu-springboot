package com.laberit.Modu.domain.model.response;

import com.laberit.Modu.domain.model.Order;
import lombok.Builder;

@Builder
public record CheckoutResult(
        Order order,
        CartWithPriceAndStockCheck cartResponse
) {
    public boolean isSuccessful() {
        return cartResponse.changedPrices().isEmpty() && cartResponse.insufficientStock().isEmpty();
    }
}
