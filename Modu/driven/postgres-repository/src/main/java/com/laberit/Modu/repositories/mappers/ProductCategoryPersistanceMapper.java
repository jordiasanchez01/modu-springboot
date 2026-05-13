package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.ProductCategory;
import com.laberit.Modu.repositories.models.ProductCategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface ProductCategoryPersistanceMapper {
    ProductCategoryEntity toEntity(ProductCategory productCategory);

    ProductCategory toDomain(ProductCategoryEntity entity);

    List<ProductCategory> toDomainList(List<ProductCategoryEntity> entities);

    List<ProductCategoryEntity> toEntityList(List<ProductCategory> categories);
}
