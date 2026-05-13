package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.PagedResult;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.rest.generated.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductRestMapper {

    ProductDetailsResponse toProductDetailsResponse(Product product);

    @Mapping(source = "id", target = "productId")
    @Mapping(source = "imageUrl", target = "url")
    ProductsResponse toProductsResponse(Product product);

    default ProductPageResponse toProductPageResponse(PagedResult<Product> pagedResult) {
        List<ProductsResponse> data = pagedResult.content()
                .stream()
                .map(this::toProductsResponse)
                .toList();

        PaginationMeta meta = new PaginationMeta()
                .page(pagedResult.page())
                .size(pagedResult.size())
                .hasNext(pagedResult.hasNext());

        return new ProductPageResponse()
                .data(data)
                .meta(meta);
    }
}
