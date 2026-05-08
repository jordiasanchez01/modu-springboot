package com.laberit.Modu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Long id;
    private Long productVariantId;
    private Double unitPrice;
    private Integer quantity;
    private Integer currentStock;

    public Double getTotalPrice() { return unitPrice * quantity; }
}


