package com.laberit.Modu.ports.driving.command;

import com.laberit.Modu.domain.model.ProductSortField;

public record SearchProductsCommand(
        String title,
        ProductSortField sort,
        Integer maxPrice,
        String category,
        Integer page,
        Integer size) {}
