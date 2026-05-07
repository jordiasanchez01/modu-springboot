package com.laberit.Modu.services;

import com.laberit.Modu.domain.model.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driving.ProductServicePort;
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
    public PagedResult<Product> search(SearchProductsCommand command) {
        ProductSearchCriteria criteria = new ProductSearchCriteria(
                command.title(),
                command.sort(),
                command.maxPrice(),
                command.category(),
                command.page(),
                command.size()
        );
       return productRepositoryPort.findAll(criteria);
    }
}
