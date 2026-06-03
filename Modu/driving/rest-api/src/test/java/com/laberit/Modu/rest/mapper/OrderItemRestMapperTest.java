package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.rest.generated.model.OrderItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemRestMapperTest {

    private OrderItemRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderItemRestMapperImpl();
    }

    private OrderItem sampleOrderItem() {
        return OrderItem.builder()
                .id(1L).orderId(10L).productId(2L).productVariantId(5L)
                .unitPrice(20.0).quantity(3)
                .build();
    }

    @Nested
    @DisplayName("toOrderItemResponse")
    class ToOrderItemResponse {

        @Test
        void shouldReturnNull_whenOrderItemIsNull() {
            assertThat(mapper.toOrderItemResponse(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            OrderItemResponse result = mapper.toOrderItemResponse(sampleOrderItem());

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getOrderId()).isEqualTo(10L);
            assertThat(result.getProductId()).isEqualTo(2L);
            assertThat(result.getProductVariantId()).isEqualTo(5L);
            assertThat(result.getUnitPrice()).isEqualTo(20.0);
            assertThat(result.getQuantity()).isEqualTo(3);
        }

        @Test
        void shouldComputeTotalPrice() {
            // totalPrice = unitPrice * quantity via OrderItem.getTotalPrice()
            OrderItemResponse result = mapper.toOrderItemResponse(sampleOrderItem());

            assertThat(result.getTotalPrice()).isEqualTo(60.0);
        }
    }

    @Nested
    @DisplayName("toOrderItemResponseList")
    class ToOrderItemResponseList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toOrderItemResponseList(null)).isNull();
        }

        @Test
        void shouldMapEachItem() {
            OrderItem item1 = OrderItem.builder().id(1L).orderId(10L).productVariantId(5L).unitPrice(10.0).quantity(2).build();
            OrderItem item2 = OrderItem.builder().id(2L).orderId(10L).productVariantId(6L).unitPrice(5.0).quantity(1).build();

            List<OrderItemResponse> result = mapper.toOrderItemResponseList(List.of(item1, item2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(1).getId()).isEqualTo(2L);
        }
    }
}
