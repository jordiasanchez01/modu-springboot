package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.ProductCategory;
import com.laberit.Modu.repositories.models.ProductCategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ProductCategoryPersistanceMapper {

    ProductCategoryEntity toEntity(ProductCategory productCategory);

    ProductCategory toDomain(ProductCategoryEntity entity);

    /*default ProductCategoryEntity toEntity(ProductCategory productCategory){
        if (productCategory==null){
            return null;
        }
        ProductCategoryEntity entity = new ProductCategoryEntity();
        entity.setId(productCategory.id());
        entity.setCategoryId(productCategory.categoryId());
        entity.setProductId(productCategory.productId());
        return entity;
    }

    default ProductCategory toDomain(ProductCategoryEntity entity){
        if (entity==null){
            return null;
        }
        return ProductCategory.builder()
                .id(entity.getId())
                .categoryId(entity.getCategoryId())
                .productId(entity.getProductId())
                .build();
    }*/

    List<ProductCategory> toDomainList(List<ProductCategoryEntity> entities);
    List<ProductCategoryEntity> toEntityList(List<ProductCategory> categories);
}
