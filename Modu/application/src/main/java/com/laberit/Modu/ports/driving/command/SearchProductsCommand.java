package com.laberit.Modu.ports.driving.command;

public record SearchProductsCommand(
        String title,
        String sort,
        Integer maxPrice,
        String category,
        Integer page,
        Integer size) {}
