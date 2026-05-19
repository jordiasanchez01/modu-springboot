package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.response.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.ports.driving.command.AddProductCommand;
import com.laberit.Modu.ports.driving.command.SearchProductsCommand;
import com.laberit.Modu.ports.driving.command.UpdateProductCommand;

public interface ProductServicePort {

    Product findProductById(Long ProductId);

    Product findProductByName(String name);

    Product addProduct(AddProductCommand command);

    Product updateProduct(UpdateProductCommand command);

    void deleteProduct(Long ProductId);

    PagedResult<Product> search(SearchProductsCommand command);
}
