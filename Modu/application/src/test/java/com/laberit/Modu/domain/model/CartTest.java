package com.laberit.Modu.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartTest {

    @Test
    void getSubTotalPrice_shouldReturnZero_whenCartHasNoItems() {
        Cart cart = Cart.builder().build();

        assertThat(cart.getSubTotalPrice()).isEqualTo(0.0);
    }

    @Test
    void getSubTotalPrice_shouldSumTotalPriceOfAllItems() {
        CartItem item1 = CartItem.builder().unitPrice(10.0).quantity(2).build(); // 20.00
        CartItem item2 = CartItem.builder().unitPrice(5.0).quantity(3).build();  // 15.00
        Cart cart = Cart.builder().cartItems(List.of(item1, item2)).build();

        assertThat(cart.getSubTotalPrice()).isEqualTo(35.0);
    }

    @Test
    void getSubTotalPrice_shouldHandleMultipleItemsOfDifferentQuantities() {
        CartItem item1 = CartItem.builder().unitPrice(3.33).quantity(3).build();  // 9.99
        CartItem item2 = CartItem.builder().unitPrice(1.00).quantity(1).build();  // 1.00
        Cart cart = Cart.builder().cartItems(List.of(item1, item2)).build();

        assertThat(cart.getSubTotalPrice()).isEqualTo(10.99);
    }

    @Test
    void getShippingCosts_shouldBeZeroByDefault() {
        Cart cart = Cart.builder().build();

        assertThat(cart.getShippingCosts()).isEqualTo(0.0);
    }

    @Test
    void getShippingCosts_shouldRoundToTwoDecimalPlaces() {
        Cart cart = Cart.builder().shippingCosts(4.505).build();

        assertThat(cart.getShippingCosts()).isEqualTo(4.51);
    }

    @Test
    void getTotalPrice_shouldAddSubTotalAndShippingCosts() {
        CartItem item = CartItem.builder().unitPrice(10.0).quantity(3).build(); // 30.00
        Cart cart = Cart.builder()
                .cartItems(List.of(item))
                .shippingCosts(5.0)
                .build();

        assertThat(cart.getTotalPrice()).isEqualTo(35.0);
    }

    @Test
    void getTotalPrice_shouldEqualSubTotalWhenShippingIsZero() {
        CartItem item = CartItem.builder().unitPrice(20.0).quantity(2).build();
        Cart cart = Cart.builder().cartItems(List.of(item)).build();

        assertThat(cart.getTotalPrice()).isEqualTo(cart.getSubTotalPrice());
    }
}
