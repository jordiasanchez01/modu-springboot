package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.repositories.ProductJpaRepository;
import com.laberit.Modu.repositories.models.ProductEntity;
import com.laberit.Modu.repositories.specifications.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {
    private final ProductJpaRepository productJpaRepository;
    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
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
