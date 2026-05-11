package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.CategoryEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Integer> {

    List<CategoryEntity> findAllByIdIn(List<Integer> id);

    Optional<CategoryEntity> findByName(String name);

    boolean existsByName(String name);

}
