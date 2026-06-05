package com.laberit.Modu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long productId;
    private Long productVariantId;
    private Double unitPrice;
    private Integer quantity;

    public Double getTotalPrice() { return BigDecimal.valueOf(unitPrice * quantity)
            .setScale(2, RoundingMode.HALF_UP)
            .doubleValue(); }
}