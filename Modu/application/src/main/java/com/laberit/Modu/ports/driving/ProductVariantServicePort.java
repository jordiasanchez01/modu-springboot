package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driving.command.AddProductVariantCommand;
import com.laberit.Modu.ports.driving.command.UpdateProductVariantCommand;

import java.util.List;
import java.util.Set;

public interface ProductVariantServicePort {

    List<ProductVariant> findAllByProductId(Long productId);

}
