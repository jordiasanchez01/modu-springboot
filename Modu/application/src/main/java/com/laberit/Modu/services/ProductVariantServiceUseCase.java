package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.ProductVariantNotAvailableException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductVariantServiceUseCase implements ProductVariantServicePort {
    private final ProductVariantRepositoryPort productVariantRepositoryPort;

    @Override
    public List<ProductVariant> findAllByProductId(Long productId) {

        return sortBySizeThenColor(
                productVariantRepositoryPort.findAllByProductId(productId));
    }

    @Override
    public List<ProductVariant> findAllByIdIn(List<Long> ids) {
        return productVariantRepositoryPort.findAllByIdIn(ids);
    }

    @Override
    public Set<ProductVariant> findAllByIdInSet(Set<Long> ids) {
        return productVariantRepositoryPort.findAllByIdInSet(ids);
    }

    @Override
    public Optional<ProductVariant> findById(Long id) {
        return productVariantRepositoryPort.findById(id);
    }

    @Override
    public void assertIsValidToPurchase(ProductVariant variant, Integer requiredStock) {
        if (!variant.getActive() || (variant.getStock() < requiredStock))
            throw new ProductVariantNotAvailableException(variant.getId(), (variant.getStock() < requiredStock));
    }

    private static final List<String> LETTERED_SIZE_ORDER =
            List.of("XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL");

    public List<ProductVariant> sortBySizeThenColor(List<ProductVariant> variants) {

        return variants.stream()
                .sorted(Comparator
                        .comparingInt(this::sizeCategory)       // numeric first, then lettered, then unknown
                        .thenComparing(this::resolveSize)        // within category, apply the right ordering
                        .thenComparing(v -> Optional.ofNullable(v.getColor()).orElse("")))
                .collect(Collectors.toList());
    }

    private int sizeCategory(ProductVariant v) {
        String size = Optional.ofNullable(v.getSize()).orElse("").trim();
        if (isNumeric(size)) return 0;
        if (LETTERED_SIZE_ORDER.contains(size.toUpperCase())) return 1;
        return 2; // unrecognized sizes go last
    }

    private String resolveSize(ProductVariant v) {
        String size = Optional.ofNullable(v.getSize()).orElse("").trim();
        if (isNumeric(size)) {
            return String.format("%010.3f", Double.parseDouble(size));
        }
        int index = LETTERED_SIZE_ORDER.indexOf(size.toUpperCase());
        if (index >= 0) {
            return String.format("%03d", index);
        }
        return size;
    }

    private boolean isNumeric(String value) {
        if (value == null || value.isBlank()) return false;
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
