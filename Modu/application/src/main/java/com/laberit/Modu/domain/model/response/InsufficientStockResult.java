package com.laberit.Modu.domain.model.response;

import lombok.Builder;

@Builder
public record InsufficientStockResult(
        Long productVariantId,
        Integer requestedQuantity,
        Integer availableStock
) {
}
