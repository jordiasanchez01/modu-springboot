package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.ProductVariantNotAvailableException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductVariantServiceUseCase implements ProductVariantServicePort {
    private final ProductVariantRepositoryPort productVariantRepositoryPort;

    @Override
    public List<ProductVariant> findAllByProductId(Long productId) {
        log.debug("Fetching all Product Variants of this Product");
        return productVariantRepositoryPort.findAllByProductId(productId);
    }

    @Override
    public Set<ProductVariant> findAllByIdIn(Set<Long> ids) {
        return productVariantRepositoryPort.findAllByIdIn(ids);
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return productVariantRepositoryPort.findById(id);
    }

    @Override
    public void assertIsValidToPurchase(Long id, Integer requiredStock) {
        ProductVariant productVariant = productVariantRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductVariantNotFoundException(id.toString()));
        if (!productVariant.getActive() || (productVariant.getStock() < requiredStock))
            throw new ProductVariantNotAvailableException(id, (productVariant.getStock() < requiredStock));
    }

}
