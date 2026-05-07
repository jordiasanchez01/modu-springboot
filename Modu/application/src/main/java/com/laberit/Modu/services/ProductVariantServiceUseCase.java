package com.laberit.Modu.services;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.AddProductVariantCommand;
import com.laberit.Modu.ports.driving.command.UpdateProductVariantCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductVariantServiceUseCase implements ProductVariantServicePort {
    private final ProductVariantRepositoryPort productVariantRepositoryPort;

    @Override
    public List<ProductVariant> findAll() {
        log.debug("Fetching all Product Categories");
        return productVariantRepositoryPort.findAll();
    }

    @Override
    public ProductVariant findProductVariantById(Long productVariantId) {
        return null;
    }

    @Override
    public List<ProductVariant> findAllByProductId(Long productId) {
        log.debug("Fetching all Product Variants of this Product");
        return productVariantRepositoryPort.findAllByProductId(productId);
    }

    @Override
    public ProductVariant findProductVariantByName(String name) {
        return null;
    }

    @Override
    public ProductVariant addProductVariant(AddProductVariantCommand command) {
        return null;
    }

    @Override
    public ProductVariant updateProductVariant(UpdateProductVariantCommand command) {
        return null;
    }

    @Override
    public void deleteProductVariant(Long productVariantId) {

    }
}
