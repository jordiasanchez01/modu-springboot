package com.laberit.Modu.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    @Test
    void getSubTotalPrice_shouldReturnZero_whenOrderHasNoItems() {
        Order order = Order.builder().build();

        assertThat(order.getSubTotalPrice()).isEqualTo(0.0);
    }

    @Test
    void getSubTotalPrice_shouldSumTotalPriceOfAllItems() {
        OrderItem item1 = OrderItem.builder().unitPrice(10.0).quantity(2).build(); // 20.00
        OrderItem item2 = OrderItem.builder().unitPrice(5.0).quantity(3).build();  // 15.00
        Order order = Order.builder().orderItems(List.of(item1, item2)).build();

        assertThat(order.getSubTotalPrice()).isEqualTo(35.0);
    }

    @Test
    void getSubTotalPrice_shouldHandleMultipleItemsOfDifferentQuantities() {
        OrderItem item1 = OrderItem.builder().unitPrice(3.33).quantity(3).build(); // 9.99
        OrderItem item2 = OrderItem.builder().unitPrice(1.00).quantity(1).build(); // 1.00
        Order order = Order.builder().orderItems(List.of(item1, item2)).build();

        assertThat(order.getSubTotalPrice()).isEqualTo(10.99);
    }

    @Test
    void getShippingCosts_shouldReturnZero_whenShippingCostsIsNull() {
        Order order = Order.builder().shippingCosts(null).build();

        assertThat(order.getShippingCosts()).isEqualTo(0.0);
    }

    @Test
    void getShippingCosts_shouldReturnZeroByDefault_whenBuiltWithoutShipping() {
        Order order = Order.builder().build();

        assertThat(order.getShippingCosts()).isEqualTo(0.0);
    }

    @Test
    void getShippingCosts_shouldRoundToTwoDecimalPlaces() {
        Order order = Order.builder().shippingCosts(4.505).build();

        assertThat(order.getShippingCosts()).isEqualTo(4.51);
    }

    @Test
    void getTotalOrderPrice_shouldAddSubTotalAndShippingCosts() {
        OrderItem item = OrderItem.builder().unitPrice(10.0).quantity(3).build(); // 30.00
        Order order = Order.builder()
                .orderItems(List.of(item))
                .shippingCosts(5.0)
                .build();

        assertThat(order.getTotalOrderPrice()).isEqualTo(35.0);
    }

    @Test
    void getTotalOrderPrice_shouldEqualSubTotalWhenShippingIsNull() {
        OrderItem item = OrderItem.builder().unitPrice(20.0).quantity(2).build(); // 40.00
        Order order = Order.builder()
                .orderItems(List.of(item))
                .shippingCosts(null)
                .build();

        assertThat(order.getTotalOrderPrice()).isEqualTo(order.getSubTotalPrice());
    }
}
