package com.laberit.Modu.domain.model;

import lombok.Builder;

import java.util.List;

@Builder
public record CartWithPriceCheck(
        Cart cart,
        List<ProductPriceChange> changedPrices
) {
}
