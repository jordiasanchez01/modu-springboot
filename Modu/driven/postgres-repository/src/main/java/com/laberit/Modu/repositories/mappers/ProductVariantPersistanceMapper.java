package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ProductVariantPersistanceMapper {

    default ProductVariantEntity toEntity(ProductVariant productVariant){
        if (productVariant==null){
            return null;
        }
        ProductVariantEntity entity = new ProductVariantEntity();
        entity.setId(productVariant.getId());
        entity.setName(productVariant.getName());
        entity.setSize(productVariant.getSize());
        entity.setColor(productVariant.getColor());
        entity.setStock(productVariant.getStock());
        entity.setActive(productVariant.getActive());
        entity.setProductId(productVariant.getProductId());
        return entity;
    }

    default ProductVariant toDomain(ProductVariantEntity entity){
        if (entity==null){
            return null;
        }
        return ProductVariant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .size(entity.getSize())
                .color(entity.getColor())
                .stock(entity.getStock())
                .active(entity.getActive())
                .productId(entity.getProductId())
                .build();
    }

    List<ProductVariant> toDomainList(List<ProductVariantEntity> entities);
}
