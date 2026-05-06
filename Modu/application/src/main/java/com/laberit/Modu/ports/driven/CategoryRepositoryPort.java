package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryPort {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String name);

    boolean existsByName(String name);

    Category save(Category category);

    void deleteById(Long id);
}
