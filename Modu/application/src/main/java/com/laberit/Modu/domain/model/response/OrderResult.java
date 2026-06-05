package com.laberit.Modu.domain.model.response;

import lombok.Builder;

@Builder
public record OrderResult(
        Boolean ok,
        String orderId
) {
}
