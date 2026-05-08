package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.ports.driving.CategoryServicePort;
import com.laberit.Modu.rest.generated.api.CategoriesApi;
import com.laberit.Modu.rest.generated.model.HomeCategoryResponse;
import com.laberit.Modu.rest.mapper.CategoryRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CategoryRestAdapter implements CategoriesApi {
    private final CategoryServicePort categoryServicePort;
    private final CategoryRestMapper mapper;

    @Override
    public ResponseEntity<List<HomeCategoryResponse>> getCategories() {
        return ResponseEntity.ok(mapper.toHomeCategoryResponseList(categoryServicePort.findAll()));
    }
}
