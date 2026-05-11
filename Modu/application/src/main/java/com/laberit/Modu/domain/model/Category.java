package com.laberit.Modu.domain.model;

import lombok.Builder;
import lombok.Data;

@Builder
public record Category(
        Integer id,
        String name
) {

}
