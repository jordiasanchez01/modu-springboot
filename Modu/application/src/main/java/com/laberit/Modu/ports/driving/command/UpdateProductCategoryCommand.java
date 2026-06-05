package com.laberit.Modu.ports.driving.command;

import com.laberit.Modu.domain.model.Category;

import java.util.List;

public record UpdateProductCategoryCommand(
        List<Category> categoriesList
) {
}
