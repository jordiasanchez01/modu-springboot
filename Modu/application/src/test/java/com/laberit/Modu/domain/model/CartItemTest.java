package com.laberit.Modu.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    @Test
    void getTotalPrice_shouldMultiplyUnitPriceByQuantity() {
        CartItem item = CartItem.builder().unitPrice(10.0).quantity(3).build();

        assertThat(item.getTotalPrice()).isEqualTo(30.0);
    }

    @Test
    void getTotalPrice_shouldRoundHalfUp() {
        // 1.005 * 1 = 1.005 → HALF_UP → 1.01
        CartItem item = CartItem.builder().unitPrice(1.005).quantity(1).build();

        assertThat(item.getTotalPrice()).isEqualTo(1.01);
    }

    @Test
    void getTotalPrice_shouldRoundDown() {
        // 1.004 * 1 = 1.004 → rounds down → 1.00
        CartItem item = CartItem.builder().unitPrice(1.004).quantity(1).build();

        assertThat(item.getTotalPrice()).isEqualTo(1.0);
    }

    @Test
    void getTotalPrice_shouldAccumulateAcrossQuantity() {
        // 0.10 * 3 = 0.30 (no precision loss via BigDecimal)
        CartItem item = CartItem.builder().unitPrice(0.10).quantity(3).build();

        assertThat(item.getTotalPrice()).isEqualTo(0.30);
    }
}
