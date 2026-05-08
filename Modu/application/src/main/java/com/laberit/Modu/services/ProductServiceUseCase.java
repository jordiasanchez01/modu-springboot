package com.laberit.Modu.services;

import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driven.CategoryRepositoryPort;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.command.AddProductCommand;
import com.laberit.Modu.ports.driving.command.UpdateProductCommand;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceUseCase implements ProductServicePort {
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public Product findProductById(Long productId) {
        return null;
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
    public void deleteProduct(Long ProductId) {}

    @Override
    public PagedResult<Product> search(SearchProductsCommand command) {
        ProductSortField sortField;
        SortDirection sortDirection;

        if (command.orderByPrice() != null) {
            sortField = ProductSortField.PRICE;
            sortDirection =command.orderByPrice().equalsIgnoreCase("ASC") ?
                    SortDirection.ASC : SortDirection.DESC;
        } else {
            sortField = ProductSortField.ID;
            sortDirection = SortDirection.DESC;
        }

        ProductSearchCriteria criteria = new ProductSearchCriteria(
                command.title(),
                sortField,
                sortDirection,
                command.maxPrice(),
                command.categoryIds(),
                command.page(),
                command.size()
        );
       return productRepositoryPort.findAll(criteria);
    }
}
