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
import java.util.Set;
import java.util.stream.Collectors;

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

        Set<Integer> categoryIds = productCategoryRepositoryPort.findAllByProductId(productId).stream()
                .map(ProductCategory::categoryId)
                .collect(Collectors.toSet());

        Set<Category> categories = categoryRepositoryPort.findAllByIdIn(categoryIds);

        product.setCategoriesSet(categories);
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
