package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.rest.generated.api.ProductVariantsApi;
import com.laberit.Modu.rest.generated.model.ProductVariantResponse;
import com.laberit.Modu.rest.mapper.ProductVariantRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ProductVariantRestAdapter implements ProductVariantsApi {
    private final ProductVariantServicePort productVariantServicePort;
    private final ProductVariantRestMapper mapper;

    @Override
    public ResponseEntity<List<ProductVariantResponse>> getAllProductVariantsByProductId(Long productId) {
        return ResponseEntity.ok(mapper.toProductVariantResponseList(productVariantServicePort.findAllByProductId(productId)));
    }
}
