package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.repositories.models.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ProductPersistanceMapper {

    default ProductEntity toEntity(Product product){
        if (product==null){
            return null;
        }
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setDescription(product.getDescription());
        entity.setImageUrl(product.getImageUrl());
        entity.setPrice(product.getPrice());
        entity.setActive(product.getActive());

        return entity;
    }


    default Product toDomain(ProductEntity entity){
        if (entity==null){
            return null;
        }
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .price(entity.getPrice())
                .active(entity.getActive())
                .build();
    }

    List<Product> toDomainList(List<ProductEntity> entities);
    List<ProductEntity> toEntityList(List<Product> products);
}
