package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.rest.generated.model.ProductVariantResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVariantRestMapper {

    ProductVariantResponse toProductVariantResponse(ProductVariant productVariant);

    List<ProductVariantResponse>  toProductVariantResponseList(List<ProductVariant> productVariantList);
}
