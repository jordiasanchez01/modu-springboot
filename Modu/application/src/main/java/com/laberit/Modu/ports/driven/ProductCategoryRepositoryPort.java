package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepositoryPort {

    Optional<ProductCategory> findById(Long id);

    List<ProductCategory> findAllByCategoryId(Integer categoryId);

    List<ProductCategory> findAllByProductId(Long productId);

    boolean existsById(Long id);

    boolean existsByCategoryId(Integer categoryId);

    boolean existsByProductId(Long productId);
}
