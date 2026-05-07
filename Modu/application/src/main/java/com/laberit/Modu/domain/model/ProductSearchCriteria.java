package com.laberit.Modu.domain.model;

import java.util.List;

public record ProductSearchCriteria(
        String title,
        ProductSortField sort,
        Integer maxPrice,
        List<Integer> categoryIds,
        Integer page,
        Integer size
) {
}
