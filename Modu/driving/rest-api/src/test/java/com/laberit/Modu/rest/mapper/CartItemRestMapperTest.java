package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.CartItemsQuantitiesUpdateDTO;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemRestMapperTest {

    private CartItemRestMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CartItemRestMapperImpl();
    }

    private CartItem sampleCartItem() {
        return CartItem.builder()
                .id(1L)
                .cartId(10L)
                .productId(2L)
                .productVariantId(5L)
                .unitPrice(10.00)
                .quantity(3)
                .currentStock(7)
                .build();
    }

    @Nested
    @DisplayName("toCartItemForCartResponse")
    class ToCartItemForCartResponse {

        @Test
        void shouldReturnNull_whenCartItemIsNull() {
            assertThat(mapper.toCartItemForCartResponse(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            CartItemForCartResponse result = mapper.toCartItemForCartResponse(sampleCartItem());

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getProductId()).isEqualTo(2L);
            assertThat(result.getProductVariantId()).isEqualTo(5L);
            assertThat(result.getCurrentStock()).isEqualTo(7);
            assertThat(result.getQuantity()).isEqualTo(3);
            assertThat(result.getUnitPrice()).isEqualTo(10.00);
            assertThat(result.getTotalPrice()).isEqualTo(30.00);
        }
    }

    @Nested
    @DisplayName("toCartItemForCartUpdate")
    class ToCartItemForCartUpdate {

        @Test
        void shouldReturnNull_whenCartItemIsNull() {
            assertThat(mapper.toCartItemForCartUpdate(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            CartItemForCartUpdate result = mapper.toCartItemForCartUpdate(sampleCartItem());

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getProductId()).isEqualTo(2L);
            assertThat(result.getProductVariantId()).isEqualTo(5L);
            assertThat(result.getQuantity()).isEqualTo(3);
            assertThat(result.getUnitPrice()).isEqualTo(10.00);
        }
    }

    @Nested
    @DisplayName("toCartItemForOrderRequest")
    class ToCartItemForOrderRequest {

        @Test
        void shouldReturnNull_whenCartItemIsNull() {
            assertThat(mapper.toCartItemForOrderRequest(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            CartItemForOrderRequest result = mapper.toCartItemForOrderRequest(sampleCartItem());

            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getProductId()).isEqualTo(2L);
            assertThat(result.getProductVariantId()).isEqualTo(5L);
            assertThat(result.getQuantity()).isEqualTo(3);
            assertThat(result.getUnitPrice()).isEqualTo(10.00);
            assertThat(result.getTotalPrice()).isEqualTo(30.00);
        }
    }

    @Nested
    @DisplayName("toCartItemQuantityUpdateRequest")
    class ToCartItemQuantityUpdateRequest {

        @Test
        void shouldReturnNull_whenCartItemIsNull() {
            assertThat(mapper.toCartItemQuantityUpdateRequest(null)).isNull();
        }

        @Test
        void shouldMapIdToCartItemIdAndQuantity() {
            CartItemQuantityUpdateRequest result = mapper.toCartItemQuantityUpdateRequest(sampleCartItem());

            assertThat(result.getCartItemId()).isEqualTo(1L);
            assertThat(result.getQuantity()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("toUpdateCartItemQuantityCommand")
    class ToUpdateCartItemQuantityCommand {

        @Test
        void shouldReturnNull_whenRequestIsNull() {
            assertThat(mapper.toUpdateCartItemQuantityCommand(null)).isNull();
        }

        @Test
        void shouldMapCartItemIdAndQuantity() {
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(1L, 5);

            UpdateCartItemQuantityCommand command = mapper.toUpdateCartItemQuantityCommand(request);

            assertThat(command.cartItemId()).isEqualTo(1L);
            assertThat(command.quantity()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("toCartItemForCartResponseList")
    class ToCartItemForCartResponseList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toCartItemForCartResponseList(null)).isNull();
        }

        @Test
        void shouldMapEachItem() {
            List<CartItemForCartResponse> result = mapper.toCartItemForCartResponseList(List.of(sampleCartItem()));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("toCartItemForCartUpdateList")
    class ToCartItemForCartUpdateList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toCartItemForCartUpdateList(null)).isNull();
        }

        @Test
        void shouldMapEachItem() {
            List<CartItemForCartUpdate> result = mapper.toCartItemForCartUpdateList(List.of(sampleCartItem()));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("toCartItemForOrderRequestList")
    class ToCartItemForOrderRequestList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toCartItemForOrderRequestList(null)).isNull();
        }

        @Test
        void shouldMapEachItem() {
            List<CartItemForOrderRequest> result = mapper.toCartItemForOrderRequestList(List.of(sampleCartItem()));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("toUpdateCartItemQuantityCommandList")
    class ToUpdateCartItemQuantityCommandList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toUpdateCartItemQuantityCommandList(null)).isNull();
        }

        @Test
        void shouldMapEachRequest() {
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(1L, 5);

            List<UpdateCartItemQuantityCommand> result = mapper.toUpdateCartItemQuantityCommandList(List.of(request));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).cartItemId()).isEqualTo(1L);
            assertThat(result.get(0).quantity()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("toCartItemListFromQuantityUpdateRequest")
    class ToCartItemListFromQuantityUpdateRequest {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toCartItemListFromQuantityUpdateRequest(null)).isNull();
        }

        @Test
        void shouldOnlyMapQuantity_notId() {
            // The @Mapping(source = "cartItemId", target = "id") annotation on the interface
            // is not applied by MapStruct for list-returning methods; only quantity is mapped.
            CartItemQuantityUpdateRequest request = new CartItemQuantityUpdateRequest(99L, 5);

            List<CartItem> result = mapper.toCartItemListFromQuantityUpdateRequest(List.of(request));

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getQuantity()).isEqualTo(5);
            assertThat(result.get(0).getId()).isNull();
        }
    }

    @Nested
    @DisplayName("toCartItemsQuantitiesUpdateDTO")
    class ToCartItemsQuantitiesUpdateDTO {

        @Test
        void shouldMapDeviceIdAndCartItemCommands() {
            UpdateCartQuantitiesRequest request = new UpdateCartQuantitiesRequest(
                    List.of(new CartItemQuantityUpdateRequest(1L, 3))
            );

            CartItemsQuantitiesUpdateDTO dto = mapper.toCartItemsQuantitiesUpdateDTO("testdevice1234", request);

            assertThat(dto.deviceId()).isEqualTo("testdevice1234");
            assertThat(dto.cartItemCommands()).hasSize(1);
            assertThat(dto.cartItemCommands().get(0).cartItemId()).isEqualTo(1L);
            assertThat(dto.cartItemCommands().get(0).quantity()).isEqualTo(3);
        }
    }
}
