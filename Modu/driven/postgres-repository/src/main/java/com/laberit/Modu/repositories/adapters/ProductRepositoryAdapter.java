package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.repositories.ProductJpaRepository;
import com.laberit.Modu.repositories.mappers.CategoryPersistanceMapper;
import com.laberit.Modu.repositories.mappers.ProductPersistanceMapper;
import com.laberit.Modu.repositories.mappers.ProductVariantPersistanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final ProductJpaRepository repository;
    private final ProductPersistanceMapper productMapper;
    private final CategoryPersistanceMapper categoryMapper;
    private final ProductVariantPersistanceMapper productVariantMapper;


    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public List<Product> findAllByProductId(Long productId) {
        return List.of();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return repository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
