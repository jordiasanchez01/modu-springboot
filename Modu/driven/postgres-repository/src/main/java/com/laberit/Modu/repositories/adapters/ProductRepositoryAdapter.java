package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.repositories.ProductJpaRepository;
import com.laberit.Modu.repositories.mappers.ProductPersistanceMapper;
import lombok.RequiredArgsConstructor;
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
    public List<Product> findAll() {


        //Here goes mapping
        return null;
    }
}
