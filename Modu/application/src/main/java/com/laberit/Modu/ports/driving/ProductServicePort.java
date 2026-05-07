package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;

public interface ProductServicePort {

    PagedResult<Product> search(SearchProductsCommand command);
}
