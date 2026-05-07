package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductSearchCriteria;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;

public interface ProductRepositoryPort {

    PagedResult<Product> findAll(ProductSearchCriteria searchCriteria);
}
