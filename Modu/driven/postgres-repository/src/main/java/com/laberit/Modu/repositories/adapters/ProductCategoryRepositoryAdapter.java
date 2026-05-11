package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.ProductCategory;
import com.laberit.Modu.ports.driven.ProductCategoryRepositoryPort;
import com.laberit.Modu.repositories.ProductCategoryJpaRepository;
import com.laberit.Modu.repositories.mappers.ProductCategoryPersistanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductCategoryRepositoryAdapter implements ProductCategoryRepositoryPort {
    private final ProductCategoryJpaRepository repository;
    private final ProductCategoryPersistanceMapper mapper;

    @Override
    public Optional<ProductCategory> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain); }

    @Override
    public List<ProductCategory> findAllByCategoryId(Integer categoryId) {
        return mapper.toDomainList(repository.findAllByCategoryId(categoryId));
    }

    @Override
    public List<ProductCategory> findAllByProductId(Long productId) {
        return mapper.toDomainList(repository.findAllByProductId(productId));
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByCategoryId(Integer categoryId) {
        return repository.existsByCategoryId(categoryId);
    }

    @Override
    public boolean existsByProductId(Long productId) {
        return repository.existsByProductId(productId);
    }
}
