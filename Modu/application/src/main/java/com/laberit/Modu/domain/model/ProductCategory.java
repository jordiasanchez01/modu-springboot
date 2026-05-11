package com.laberit.Modu.domain.model;

import lombok.Builder;

@Builder
public record ProductCategory(
        Long id,
        Integer categoryId,
        Long productId
) {

}
