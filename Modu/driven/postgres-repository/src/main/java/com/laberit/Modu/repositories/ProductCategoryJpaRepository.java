package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.CategoryEntity;
import com.laberit.Modu.repositories.models.ProductCategoryEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, Integer> {

    @NullMarked
    Optional<ProductCategoryEntity> findById(Long id);

    List<ProductCategoryEntity> findAllByCategoryId(Integer categoryId);

    List<ProductCategoryEntity> findAllByProductId(Long productId);

    boolean existsById(Long id);

    boolean existsByCategoryId(Integer categoryId);

    boolean existsByProductId(Long productId);
}
