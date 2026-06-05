package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.response.*;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartRestMapperTest {

    private CartRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CartRestMapperImpl();
    }

    private CartItem sampleCartItem() {
        return CartItem.builder()
                .id(1L).cartId(10L).productId(2L).productVariantId(5L)
                .unitPrice(10.0).quantity(3).currentStock(7)
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
    @DisplayName("toCartResponse")
    class ToCartResponse {

        @Test
        void shouldMapDeviceId() {
            Cart cart = Cart.builder().deviceId("device-abc").build();

            CartResponse result = mapper.toCartResponse(cart);

            assertThat(result.getDeviceId()).isEqualTo("device-abc");
        }

        @Test
        void shouldComputeSubTotalPriceAndTotalPrice() {
            // subTotalPrice and totalPrice are injected via @Mapping expressions,
            // not plain field mapping — they come from Cart.getSubTotalPrice() and getTotalPrice().
            CartItem item = CartItem.builder().unitPrice(10.0).quantity(2).build(); // 20.00
            Cart cart = Cart.builder().shippingCosts(5.0).cartItems(List.of(item)).build();

            CartResponse result = mapper.toCartResponse(cart);

            assertThat(result.getSubTotalPrice()).isEqualTo(20.0);
            assertThat(result.getTotalPrice()).isEqualTo(25.0);
            assertThat(result.getShippingCosts()).isEqualTo(5.0);
        }

        @Test
        void shouldMapTimestamps_asOffsetDateTimeAtUTC() {
            Instant now = Instant.parse("2025-06-01T12:00:00Z");
            Cart cart = Cart.builder().createdAt(now).updatedAt(now).build();

            CartResponse result = mapper.toCartResponse(cart);

            assertThat(result.getCreatedAt().toInstant()).isEqualTo(now);
            assertThat(result.getUpdatedAt().toInstant()).isEqualTo(now);
        }

        @Test
        void shouldMapCartItems() {
            Cart cart = Cart.builder().cartItems(List.of(sampleCartItem())).build();

            CartResponse result = mapper.toCartResponse(cart);

            assertThat(result.getCartItems()).hasSize(1);
            assertThat(result.getCartItems().get(0).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("toCartUpdatedAtResponse")
    class ToCartUpdatedAtResponse {

        @Test
        void shouldMapInstantToUpdatedAt_asOffsetDateTimeAtUTC() {
            Instant now = Instant.parse("2025-06-01T08:30:00Z");

            CartUpdatedAtResponse result = mapper.toCartUpdatedAtResponse(now);

            assertThat(result.getUpdatedAt().toInstant()).isEqualTo(now);
            assertThat(result.getUpdatedAt().getOffset()).isEqualTo(ZoneOffset.UTC);
        }
    }

    @Nested
    @DisplayName("toAddCartItemCommand")
    class ToAddCartItemCommand {

        @Test
        void shouldMapDeviceIdVariantIdAndQuantity() {
            AddItemRequest request = new AddItemRequest(5L, 2);

            AddCartItemCommand result = mapper.toAddCartItemCommand("device-abc", request);

            assertThat(result.deviceId()).isEqualTo("device-abc");
            assertThat(result.productVariantId()).isEqualTo(5L);
            assertThat(result.quantity()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("toAddItemRequest")
    class ToAddItemRequest {

        @Test
        void shouldMapVariantIdAndQuantity() {
            AddCartItemCommand command = new AddCartItemCommand("device-abc", 5L, 3);

            AddItemRequest result = mapper.toAddItemRequest(command);

            assertThat(result.getVariantId()).isEqualTo(5L);
            assertThat(result.getQuantity()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("toValidatedCartResponse")
    class ToValidatedCartResponse {

        @Test
        void shouldMapCartToCartSummary() {
            Cart cart = Cart.builder().deviceId("device-abc").build();
            CartWithAllChecks input = CartWithAllChecks.builder()
                    .cart(cart)
                    .changedPrices(List.of())
                    .insufficientStock(List.of())
                    .unavailableVariants(List.of())
                    .build();

            ValidatedCartResponse result = mapper.toValidatedCartResponse(input);

            assertThat(result.getCartSummary()).isNotNull();
            assertThat(result.getCartSummary().getDeviceId()).isEqualTo("device-abc");
        }

        @Test
        void shouldMapChangedPricesToPriceChangedAlertCartItems() {
            ProductPriceChange priceChange = ProductPriceChange.builder()
                    .productVariantId(5L).oldPrice(10.0).newPrice(12.0).build();
            CartWithAllChecks input = CartWithAllChecks.builder()
                    .cart(Cart.builder().build())
                    .changedPrices(List.of(priceChange))
                    .insufficientStock(List.of())
                    .unavailableVariants(List.of())
                    .build();

            ValidatedCartResponse result = mapper.toValidatedCartResponse(input);

            assertThat(result.getPriceChangedAlert().getCartItems()).hasSize(1);
            assertThat(result.getPriceChangedAlert().getCartItems().get(0).getProductVariantId()).isEqualTo(5L);
        }

        @Test
        void shouldMapInsufficientStockToInsufficientStockAlertCartItems() {
            InsufficientStockResult stockResult = InsufficientStockResult.builder()
                    .productVariantId(5L).requestedQuantity(10).availableStock(3).build();
            CartWithAllChecks input = CartWithAllChecks.builder()
                    .cart(Cart.builder().build())
                    .changedPrices(List.of())
                    .insufficientStock(List.of(stockResult))
                    .unavailableVariants(List.of())
                    .build();

            ValidatedCartResponse result = mapper.toValidatedCartResponse(input);

            assertThat(result.getInsufficientStockAlert().getCartItems()).hasSize(1);
            assertThat(result.getInsufficientStockAlert().getCartItems().get(0).getProductVariantId()).isEqualTo(5L);
        }

        @Test
        void shouldMapUnavailableVariantsToVariantAvailabilityAlertCartItems() {
            ProductVariantAvailabilityResult unavailable = ProductVariantAvailabilityResult.builder()
                    .cartItemId(1L).productVariantId(5L).isVariantAvailable(false).build();
            CartWithAllChecks input = CartWithAllChecks.builder()
                    .cart(Cart.builder().build())
                    .changedPrices(List.of())
                    .insufficientStock(List.of())
                    .unavailableVariants(List.of(unavailable))
                    .build();

            ValidatedCartResponse result = mapper.toValidatedCartResponse(input);

            assertThat(result.getVariantAvailabilityAlert().getCartItems()).hasSize(1);
            assertThat(result.getVariantAvailabilityAlert().getCartItems().get(0).getCartItemId()).isEqualTo(1L);
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
                    .productVariantId(5L).oldPrice(10.0).newPrice(12.0).build();

            List<ProductPriceChangeResponse> result = mapper.toProductPriceChangeResponseList(List.of(priceChange));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getProductVariantId()).isEqualTo(5L);
            assertThat(result.get(0).getOldPrice()).isEqualTo(10.0);
            assertThat(result.get(0).getNewPrice()).isEqualTo(12.0);
        }
    }

    @Nested
    @DisplayName("toInsufficientStockResponseList")
    class ToInsufficientStockResponseList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toInsufficientStockResponseList(null)).isNull();
        }

        @Test
        void shouldMapEachStockResult() {
            InsufficientStockResult stockResult = InsufficientStockResult.builder()
                    .productVariantId(5L).requestedQuantity(10).availableStock(3).build();

            List<InsufficientStockResponse> result = mapper.toInsufficientStockResponseList(List.of(stockResult));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getProductVariantId()).isEqualTo(5L);
            assertThat(result.get(0).getRequestedQuantity()).isEqualTo(10);
            assertThat(result.get(0).getAvailableStock()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("toVariantAvailabilityResponseList")
    class ToVariantAvailabilityResponseList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toVariantAvailabilityResponseList(null)).isNull();
        }

        @Test
        void shouldMapEachAvailabilityResult() {
            ProductVariantAvailabilityResult result1 = ProductVariantAvailabilityResult.builder()
                    .cartItemId(1L).productVariantId(5L).isVariantAvailable(false).build();

            List<ProductVariantAvailabilityResponse> result = mapper.toVariantAvailabilityResponseList(List.of(result1));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getCartItemId()).isEqualTo(1L);
            assertThat(result.get(0).getProductVariantId()).isEqualTo(5L);
            assertThat(result.get(0).getIsVariantAvailable()).isFalse();
        }
    }
}
