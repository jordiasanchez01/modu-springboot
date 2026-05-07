package com.laberit.Modu.services;

import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.command.AddProductCommand;
import com.laberit.Modu.ports.driving.command.UpdateProductCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceUseCase implements ProductServicePort {
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public List<Product> findAll() {
        log.debug("Fetching all Product Categories");
        return productRepositoryPort.findAll();
    }

    @Override
    public Product findProductById(Long productId) {
        return null;
    }

    @Override
    public List<Product> findAllByProductId(Long productId) {
        log.debug("Fetching all Product Variants of this Product");
        return productRepositoryPort.findAllByProductId(productId);
    }

    @Override
    public Product findProductByName(String name) {
        return null;
    }

    @Override
    public Product addProduct(AddProductCommand command) {
        return null;
    }

    @Override
    public Product updateProduct(UpdateProductCommand command) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }
}
