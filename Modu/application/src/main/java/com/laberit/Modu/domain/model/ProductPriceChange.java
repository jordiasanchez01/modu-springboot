package com.laberit.Modu.domain.model;

import lombok.Builder;

@Builder
public record ProductPriceChange(
        Long productVariantId,
        Double oldPrice,
        Double newPrice
) {
}
