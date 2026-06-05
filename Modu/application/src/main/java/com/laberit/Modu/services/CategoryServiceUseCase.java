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
        return categoryRepositoryPort.findAll();
    }

    @Override
    public Category findCategoryById(Integer categoryId) {

        return categoryRepositoryPort.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId.toString()));
    }
}
