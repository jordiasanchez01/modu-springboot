package com.laberit.Modu.ports.driving.command;

import java.util.List;

public record SearchProductsCommand(
        String title,
        String orderByPrice,
        Integer maxPrice,
        List<Integer> categoryIds,
        Integer page,
        Integer size) {}
