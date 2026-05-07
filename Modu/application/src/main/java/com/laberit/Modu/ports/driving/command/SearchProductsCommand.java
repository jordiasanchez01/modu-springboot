package com.laberit.Modu.ports.driving.command;

import com.laberit.Modu.domain.model.ProductSortField;

import java.util.List;

public record SearchProductsCommand(
        String title,
        ProductSortField sort,
        Integer maxPrice,
        List<Integer> categoryIds,
        Integer page,
        Integer size) {}
