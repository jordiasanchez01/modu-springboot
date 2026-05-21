package com.laberit.Modu.domain.model;

import java.util.List;

public record ProductSearchCriteria(
        String title,
        ProductSortField sortField,
        SortDirection sortDirection,
        Integer maxPrice,
        List<String> categories,
        Integer page,
        Integer size
) {
}
