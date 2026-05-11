package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.ports.driving.command.AddCategoryCommand;
import com.laberit.Modu.ports.driving.command.UpdateCategoryCommand;

import java.util.List;

public interface CategoryServicePort {

    List<Category> findAll();

    Category findCategoryById(Integer categoryId);

    Category findCategoryByName(String name);

    Category addCategory(AddCategoryCommand command);

    Category updateCategory(UpdateCategoryCommand command);

    void deleteCategory(Long categoryId);

}
