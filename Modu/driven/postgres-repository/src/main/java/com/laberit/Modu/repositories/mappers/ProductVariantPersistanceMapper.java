package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.repositories.models.ProductEntity;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

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

        ProductEntity productRef = new ProductEntity();
        productRef.setId(productVariant.getProductId());
        entity.setProduct(productRef);
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
                .productId(entity.getProduct().getId())
                .build();
    }

    List<ProductVariant> toDomainList(List<ProductVariantEntity> entitiesList);
    List<ProductVariantEntity> toEntityList(List<ProductVariant> variantsList);

    Set<ProductVariant> toDomainSet(Set<ProductVariantEntity> entities);
}
