package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    List<Product> findAll();

    List<Product> findAllByProductId(Long productId);

    Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);

    boolean existsById(Long id);

    boolean existsByName(String name);

    Product save(Product product);

    void deleteById(Long id);
}
