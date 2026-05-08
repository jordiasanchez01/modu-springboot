package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.repositories.ProductVariantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductVariantRepositoryAdapter implements ProductVariantRepositoryPort {
    private final ProductVariantJpaRepository repository;

    @Override
    public List<ProductVariant> findAll() {
        return null;
    }

    @Override
    public List<ProductVariant> findAllByProductId(Long productId) {
        return null;
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return null;
    }

    @Override
    public Optional<ProductVariant> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public ProductVariant save(ProductVariant category) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
