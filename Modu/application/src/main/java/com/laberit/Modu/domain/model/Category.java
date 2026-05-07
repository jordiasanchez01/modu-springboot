package com.laberit.Modu.domain.model;

import lombok.Builder;

@Builder
public record Category(
        Integer id,
        String name
) {

}
