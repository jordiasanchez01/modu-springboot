package com.laberit.Modu.domain.model.response;

import lombok.Builder;

@Builder
public record ProductVariantAvailabilityResult(
        Long cartItemId,
        Long productVariantId,
        Boolean isVariantAvailable
) {
}
