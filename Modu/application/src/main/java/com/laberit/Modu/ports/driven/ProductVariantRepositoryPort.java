package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.ProductVariant;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductVariantRepositoryPort {

    List<ProductVariant> findAllByProductId(Long productId);

    Optional<ProductVariant> findById(Long id);

    Optional<ProductVariant> findByName(String name);

    boolean existsById(Long id);

    boolean existsByName(String name);

    ProductVariant save(ProductVariant productVariant);

    void deleteById(Long id);
}
