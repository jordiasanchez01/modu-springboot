package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.CategoryEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Integer> {
    @NullMarked
    List<CategoryEntity> findAll();
    @NullMarked
    Optional<CategoryEntity> findById(Integer id);

    Optional<CategoryEntity> findByName(String name);

    boolean existsById(Integer id);

    boolean existsByName(String name);

}
