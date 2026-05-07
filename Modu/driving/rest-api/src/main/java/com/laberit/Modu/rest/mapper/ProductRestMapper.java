package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.rest.generated.model.ProductDetailsResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductRestMapper {

    ProductDetailsResponse toProductDetailsResponse(Product product);

    List<ProductDetailsResponse>  toProductDetailsResponseList(List<Product> products);
}
