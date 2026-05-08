package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CategoryNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driven.CategoryRepositoryPort;
import com.laberit.Modu.ports.driven.ProductCategoryRepositoryPort;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
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
    private final CategoryRepositoryPort categoryRepositoryPort;
    private final ProductCategoryRepositoryPort productCategoryRepositoryPort;
    private final ProductVariantRepositoryPort productVariantRepositoryPort;

    @Override
    public Product findProductById(Long productId) {

        Product product = productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));

        List<Category> categories = productCategoryRepositoryPort.findAllByProductId(productId).stream()
                .map(pC -> categoryRepositoryPort.findById(pC.categoryId())
                        .orElseThrow(() -> new CategoryNotFoundException(pC.categoryId().toString())))
                .toList();

        product.setCategoriesList(categories);
        product.setProductVariantsList(productVariantRepositoryPort.findAllByProductId(productId));

        return product;
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
    public void deleteProduct(Long ProductId) {

    }



}
