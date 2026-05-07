package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Optional<Product> findById(Long id);

    Optional<Product> findByName(String name);

    boolean existsById(Long id);

    boolean existsByName(String name);

    Product save(Product product);

    void deleteById(Long id);

    PagedResult<Product> findAll(ProductSearchCriteria searchCriteria);
}
