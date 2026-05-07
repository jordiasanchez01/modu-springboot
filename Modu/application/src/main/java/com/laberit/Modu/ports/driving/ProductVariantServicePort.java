package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driving.command.AddProductVariantCommand;
import com.laberit.Modu.ports.driving.command.UpdateProductVariantCommand;

import java.util.List;

public interface ProductVariantServicePort {

    List<ProductVariant> findAll();

    List<ProductVariant> findAllByProductId(Long productId);

    ProductVariant findProductVariantById(Long productVariantId);

    ProductVariant findProductVariantByName(String name);

    ProductVariant addProductVariant(AddProductVariantCommand command);

    ProductVariant updateProductVariant(UpdateProductVariantCommand command);

    void deleteProductVariant(Long productVariantId);

}
