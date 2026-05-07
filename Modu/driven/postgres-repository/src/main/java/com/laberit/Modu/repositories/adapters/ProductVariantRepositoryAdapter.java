package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.repositories.ProductVariantJpaRepository;
import com.laberit.Modu.repositories.mappers.ProductVariantPersistanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductVariantRepositoryAdapter implements ProductVariantRepositoryPort {
    private final ProductVariantJpaRepository repository;
    private final ProductVariantPersistanceMapper mapper;

    @Override
    public List<ProductVariant> findAll() {
        return mapper.toDomainList(repository.findAll());
    }

    @Override
    public List<ProductVariant> findAllByProductId(Long productId) {
        return mapper.toDomainList(repository.findAllByProductId(productId));
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ProductVariant> findByName(String name) {
        return repository.findByName(name).map(mapper::toDomain);
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
        return mapper.toDomain(repository.save(mapper.toEntity(category)));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
