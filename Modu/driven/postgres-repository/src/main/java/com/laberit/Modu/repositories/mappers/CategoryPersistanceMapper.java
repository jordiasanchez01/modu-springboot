package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Category;
import com.laberit.Modu.repositories.models.CategoryEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface CategoryPersistanceMapper {

    default CategoryEntity toEntity(Category category){
        if (category==null){
            return null;
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setName(category.getName());
        return entity;
    }

    default Category toDomain(CategoryEntity entity){
        if (entity==null){
            return null;
        }
        return Category.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    List<Category> toDomainList(List<CategoryEntity> entities);
    List<CategoryEntity> toEntityList(List<Category> categories);
}
