package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.model.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.repositories.ProductJpaRepository;
import com.laberit.Modu.repositories.mappers.CategoryPersistanceMapper;
import com.laberit.Modu.repositories.mappers.ProductPersistanceMapper;
import com.laberit.Modu.repositories.mappers.ProductVariantPersistanceMapper;
import com.laberit.Modu.repositories.models.ProductEntity;
import com.laberit.Modu.repositories.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final ProductJpaRepository productJpaRepository;
    private final ProductPersistanceMapper productMapper;

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(productMapper::toDomain);
    }

    @Override
    public PagedResult<Product> findAll(ProductSearchCriteria searchCriteria) {
        String sortFieldName = switch (searchCriteria.sort()) {
            case PRICE -> "price";
            case CREATED_AT -> "createdAt";
        };

        PageRequest pageRequest= PageRequest.of(
                searchCriteria.page(),
                searchCriteria.size(),
                Sort.by(Sort.Direction.DESC, sortFieldName)
        );


        Specification<ProductEntity> specification =
                ProductSpecification.getSpecification(searchCriteria);

        Page<ProductEntity> page = productJpaRepository.findAll(specification, pageRequest);

        //Here goes mapping
        return null;
    }
}
