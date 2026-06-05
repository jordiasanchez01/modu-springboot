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
public class Order {
    private Long id;
    private String deviceId;
    private Instant createdAt;
    private Double shippingCosts;
    private String specialInstructions;
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public Double getSubTotalPrice(){
        return BigDecimal.valueOf(
                        orderItems.stream()
                                .mapToDouble(OrderItem::getTotalPrice)
                                .sum())
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getShippingCosts(){
        return BigDecimal.valueOf(shippingCosts != null ? shippingCosts : 0.00).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getTotalOrderPrice() {
        return getSubTotalPrice() + getShippingCosts();
    }
}
