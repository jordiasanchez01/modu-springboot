package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Optional<Product> findById(Long id);

    List<Product> findAll();
}
