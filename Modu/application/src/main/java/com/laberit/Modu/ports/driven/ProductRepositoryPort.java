package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.response.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductSearchCriteria;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepositoryPort {

    Optional<Product> findById(Long id);

    PagedResult<Product> findAll(ProductSearchCriteria searchCriteria);

    Set<Product> findAllByIdInSet(Set<Long> productIds);

    List<Product> findAllByIdIn(List<Long> productIds);
}
