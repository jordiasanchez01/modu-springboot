package com.laberit.Modu.domain.model;

import lombok.Builder;

import java.util.List;

@Builder
public record GetCartResponse(
        Cart cart,
        List<ProductPriceChange> changedPrices
) {
}
