package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.repositories.models.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface CategoryPersistanceMapper {

    CategoryEntity toEntity(Category category);
    Category toDomain(CategoryEntity entity);

    List<Category> toDomainList(List<CategoryEntity> entities);
    List<CategoryEntity> toEntityList(List<Category> categories);
}
