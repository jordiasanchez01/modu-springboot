package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface ProductVariantJpaRepository extends JpaRepository<ProductVariantEntity, Long> {

    List<ProductVariantEntity> findAll();

    List<ProductVariantEntity> findAllByProductId(Long id);

    Optional<ProductVariantEntity> findById(Long id);

    Optional<ProductVariantEntity> findByName(String name);

    boolean existsById(Long id);

    boolean existsByName(String name);

}
