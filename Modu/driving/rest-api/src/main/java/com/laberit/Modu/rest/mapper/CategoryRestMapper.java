package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryRestMapper {

    HomeCategoryResponse toHomeCategoryResponse(Category category);

    List<HomeCategoryResponse>  toHomeCategoryResponseList(List<Category> categories);
}
