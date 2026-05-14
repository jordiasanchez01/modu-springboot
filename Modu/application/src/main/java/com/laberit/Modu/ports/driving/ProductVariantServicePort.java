package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.ProductVariant;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductVariantServicePort {

    List<ProductVariant> findAllByProductId(Long productId);

    Set<ProductVariant> findAllByIdIn(Set<Long> ids);

    Optional<ProductVariant> findById(Long id);

    void assertIsValidToPurchase(Long id, Integer requiredStock);
}
