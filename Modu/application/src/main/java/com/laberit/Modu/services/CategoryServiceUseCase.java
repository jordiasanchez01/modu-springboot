package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CategoryNotFoundException;
import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.ports.driven.CategoryRepositoryPort;
import com.laberit.Modu.ports.driving.CategoryServicePort;
import com.laberit.Modu.ports.driving.command.AddCategoryCommand;
import com.laberit.Modu.ports.driving.command.UpdateCategoryCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceUseCase implements CategoryServicePort {
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    public List<Category> findAll() {
        log.debug("Fetching all Product Categories");
        return categoryRepositoryPort.findAll();
    }

    @Override
    public Category findCategoryById(Integer categoryId) {

        return categoryRepositoryPort.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId.toString()));
    }

    @Override
    public Category findCategoryByName(String name) {
        return null;
    }

    @Override
    public Category addCategory(AddCategoryCommand command) {
        return null;
    }

    @Override
    public Category updateCategory(UpdateCategoryCommand command) {
        return null;
    }

    @Override
    public void deleteCategory(Long categoryId) {

    }
}
