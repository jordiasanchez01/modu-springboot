package com.laberit.Modu.domain.model.response;

import lombok.Builder;

@Builder
public record ProductPriceChange(
        Long productVariantId,
        Double oldPrice,
        Double newPrice
) {
}
