package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.rest.generated.api.ProductsApi;
import com.laberit.Modu.rest.generated.model.ProductDetailsResponse;
import com.laberit.Modu.rest.generated.model.ProductPageResponse;
import com.laberit.Modu.rest.generated.model.ProductResponse;
import com.laberit.Modu.rest.mapper.ProductRestMapper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProductRestAdapter implements ProductsApi {
    private final ProductServicePort productServicePort;
    private final ProductRestMapper mapper;


    @Override
    public ResponseEntity<ProductDetailsResponse> getProductById(Long productId) {
        return ResponseEntity.ok(
                mapper.toProductDetailsResponse(productServicePort.findProductById(productId))
        );
    }

    @Override
    public ResponseEntity<ProductPageResponse> getProducts(Integer page, Integer size, @Nullable String title, @Nullable String orderByPrice, @Nullable Integer maxPrice, @Nullable List<String> category) {
        return null;
    }


}
