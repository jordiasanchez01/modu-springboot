package com.laberit.Modu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long id;
    private String deviceId;
    private Instant createdAt;
    private Instant updatedAt;
    @Builder.Default
    private double shippingCosts = 0.00;
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    public Double getSubTotalPrice(){
        return BigDecimal.valueOf(
                cartItems.stream()
                        .mapToDouble(CartItem::getTotalPrice)
                        .sum())
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getShippingCosts(){
        return BigDecimal.valueOf(shippingCosts).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getTotalPrice() {
        return getSubTotalPrice() + getShippingCosts();
    }
}
