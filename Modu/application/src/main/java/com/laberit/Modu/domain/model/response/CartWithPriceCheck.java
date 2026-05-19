package com.laberit.Modu.domain.model.response;

import com.laberit.Modu.domain.model.Cart;
import lombok.Builder;

import java.util.List;

@Builder
public record CartWithPriceCheck(
        Cart cart,
        List<ProductPriceChange> changedPrices
) {
}
