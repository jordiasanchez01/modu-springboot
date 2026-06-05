package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import com.laberit.Modu.ports.driving.command.AddOrderItemCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRestMapperTest {

    private OrderRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderRestMapperImpl();
    }

    private Order sampleOrder() {
        OrderItem item = OrderItem.builder()
                .id(10L).orderId(1L).productVariantId(5L)
                .unitPrice(10.0).quantity(3)
                .build();
        return Order.builder()
                .id(1L).deviceId("device-abc")
                .specialInstructions("Leave at door")
                .shippingCosts(5.0)
                .createdAt(Instant.parse("2025-06-01T12:00:00Z"))
                .orderItems(List.of(item))
                .build();
    }

    @Nested
    @DisplayName("map(Instant)")
    class MapInstant {

        @Test
        void shouldReturnNull_whenInstantIsNull() {
            assertThat(mapper.map(null)).isNull();
        }

        @Test
        void shouldConvertToOffsetDateTimeAtUTC() {
            Instant instant = Instant.parse("2025-06-01T12:00:00Z");

            var result = mapper.map(instant);

            assertThat(result).isNotNull();
            assertThat(result.getOffset()).isEqualTo(ZoneOffset.UTC);
            assertThat(result.toInstant()).isEqualTo(instant);
        }
    }

    @Nested
    @DisplayName("toOrderResponse")
    class ToOrderResponse {

        @Test
        void shouldMapHeaderFields() {
            OrderResponse result = mapper.toOrderResponse(sampleOrder());

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getDeviceId()).isEqualTo("device-abc");
            assertThat(result.getSpecialInstructions()).isEqualTo("Leave at door");
            assertThat(result.getShippingCosts()).isEqualTo(5.0);
        }

        @Test
        void shouldComputeSubtotalPriceAndTotalPrice() {
            // subtotalPrice = getSubTotalPrice() and totalPrice = getTotalOrderPrice()
            // injected via @Mapping(expression = ...) — not plain field copy.
            // With item unitPrice=10.0, quantity=3 → subtotal=30.0, total=35.0 (shipping=5.0)
            OrderResponse result = mapper.toOrderResponse(sampleOrder());

            assertThat(result.getSubtotalPrice()).isEqualTo(30.0);
            assertThat(result.getTotalPrice()).isEqualTo(35.0);
        }

        @Test
        void shouldMapCreatedAt_asOffsetDateTimeAtUTC() {
            OrderResponse result = mapper.toOrderResponse(sampleOrder());

            assertThat(result.getCreatedAt()).isNotNull();
            assertThat(result.getCreatedAt().toInstant())
                    .isEqualTo(Instant.parse("2025-06-01T12:00:00Z"));
            assertThat(result.getCreatedAt().getOffset()).isEqualTo(ZoneOffset.UTC);
        }

        @Test
        void shouldMapOrderItems() {
            OrderResponse result = mapper.toOrderResponse(sampleOrder());

            assertThat(result.getOrderItems()).hasSize(1);
            assertThat(result.getOrderItems().get(0).getId()).isEqualTo(10L);
            assertThat(result.getOrderItems().get(0).getProductVariantId()).isEqualTo(5L);
        }
    }

    @Nested
    @DisplayName("toProductPriceChangeResponse")
    class ToProductPriceChangeResponse {

        @Test
        void shouldReturnNull_whenPriceChangeIsNull() {
            assertThat(mapper.toProductPriceChangeResponse(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            ProductPriceChange priceChange = ProductPriceChange.builder()
                    .productVariantId(5L).oldPrice(10.0).newPrice(12.5).build();

            ProductPriceChangeResponse result = mapper.toProductPriceChangeResponse(priceChange);

            assertThat(result.getProductVariantId()).isEqualTo(5L);
            assertThat(result.getOldPrice()).isEqualTo(10.0);
            assertThat(result.getNewPrice()).isEqualTo(12.5);
        }
    }

    @Nested
    @DisplayName("toAddItemRequest")
    class ToAddItemRequest {

        @Test
        void shouldMapVariantIdAndQuantity() {
            AddOrderItemCommand command = new AddOrderItemCommand(1L, 5L, 3);

            AddItemRequest result = mapper.toAddItemRequest(command);

            assertThat(result.getVariantId()).isEqualTo(5L);
            assertThat(result.getQuantity()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("toAddOrderItemCommand")
    class ToAddOrderItemCommand {

        @Test
        void shouldMapUserIdVariantIdAndQuantity() {
            AddItemRequest request = new AddItemRequest(5L, 3);

            AddOrderItemCommand result = mapper.toAddOrderItemCommand(42L, request);

            assertThat(result.userId()).isEqualTo(42L);
            assertThat(result.productVariantId()).isEqualTo(5L);
            assertThat(result.quantity()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("toAddOrderCommand")
    class ToAddOrderCommand {

        @Test
        void shouldMapScalarFields() {
            AddOrderRequest request = new AddOrderRequest(true, "Leave at door", 5.0, new CartToOrder());

            AddOrderCommand result = mapper.toAddOrderCommand(request);

            assertThat(result.isPaid()).isTrue();
            assertThat(result.specialInstructions()).isEqualTo("Leave at door");
            assertThat(result.shippingCosts()).isEqualTo(5.0);
        }
    }

    @Nested
    @DisplayName("toAddOrderRequest")
    class ToAddOrderRequest {

        @Test
        void shouldMapScalarFields() {
            AddOrderCommand command = new AddOrderCommand(true, "Leave at door", 5.0, null);

            AddOrderRequest result = mapper.toAddOrderRequest(command);

            assertThat(result.getIsPaid()).isTrue();
            assertThat(result.getSpecialInstructions()).isEqualTo("Leave at door");
            assertThat(result.getShippingCosts()).isEqualTo(5.0);
        }
    }

    @Nested
    @DisplayName("toOrderResponseList")
    class ToOrderResponseList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toOrderResponseList(null)).isNull();
        }

        @Test
        void shouldMapEachOrder() {
            Order order1 = Order.builder().id(1L).deviceId("device-001").shippingCosts(0.0).build();
            Order order2 = Order.builder().id(2L).deviceId("device-002").shippingCosts(0.0).build();

            List<OrderResponse> result = mapper.toOrderResponseList(List.of(order1, order2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(1).getDeviceId()).isEqualTo("device-002");
        }
    }

    @Nested
    @DisplayName("toProductPriceChangeResponseList")
    class ToProductPriceChangeResponseList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toProductPriceChangeResponseList(null)).isNull();
        }

        @Test
        void shouldMapEachPriceChange() {
            ProductPriceChange priceChange = ProductPriceChange.builder()
                    .productVariantId(5L).oldPrice(10.0).newPrice(12.5).build();

            List<ProductPriceChangeResponse> result = mapper.toProductPriceChangeResponseList(List.of(priceChange));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getNewPrice()).isEqualTo(12.5);
        }
    }
}
