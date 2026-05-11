package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepositoryPort {

    List<Category> findAll();

    Set<Category> findAllByIdIn(Set<Integer> id);

    Optional<Category> findById(Integer id);

    Optional<Category> findByName(String name);

    boolean existsById(Integer id);

    boolean existsByName(String name);

    Category save(Category category);

    void deleteById(Integer id);
}
