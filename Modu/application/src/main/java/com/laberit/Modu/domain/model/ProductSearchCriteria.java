package com.laberit.Modu.domain.model;

public record ProductSearchCriteria(
        String title,
        ProductSortField sort,
        Integer maxPrice,
        String category,
        Integer page,
        Integer size
) {
}
