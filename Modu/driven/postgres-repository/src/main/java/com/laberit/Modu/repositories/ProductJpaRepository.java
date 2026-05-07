package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.ProductEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {

    List<ProductEntity> findAll();

    List<ProductEntity> findAllByProductId(Long id);

    Optional<ProductEntity> findById(Long id);

    Optional<ProductEntity> findByName(String name);

    boolean existsById(Long id);

    boolean existsByName(String name);

}
