package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.domain.model.response.CartWithAllChecks;
import com.laberit.Modu.ports.driven.*;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceUseCaseTest {

    @Mock private CartRepositoryPort cartRepositoryPort;
    @Mock private CartItemRepositoryPort cartItemRepositoryPort;
    @Mock private ProductVariantRepositoryPort productVariantRepositoryPort;
    @Mock private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CartServiceUseCase cartService;

    private static final String DEVICE_ID = "1000000000000001";

    @Nested
    @DisplayName("initializeCart()")
    class InitializeCart {

        @Test
        void shouldCreateNewCart_whenNoneExists() {
            Cart savedCart = Cart.builder().id(1L).deviceId(DEVICE_ID).build();
            when(cartRepositoryPort.existsByDeviceId(DEVICE_ID)).thenReturn(false);
            when(cartRepositoryPort.save(any(Cart.class))).thenReturn(savedCart);

            Cart result = cartService.initializeCart(DEVICE_ID);

            assertThat(result.getDeviceId()).isEqualTo(DEVICE_ID);
            verify(cartRepositoryPort, never()).deleteByDeviceId(any());
            verify(cartRepositoryPort).save(any(Cart.class));
        }

        @Test
        void shouldDeleteExistingCartThenCreateNew() {
            Cart savedCart = Cart.builder().id(1L).deviceId(DEVICE_ID).build();
            when(cartRepositoryPort.existsByDeviceId(DEVICE_ID)).thenReturn(true);
            when(cartRepositoryPort.save(any(Cart.class))).thenReturn(savedCart);

            cartService.initializeCart(DEVICE_ID);

            verify(cartRepositoryPort).deleteByDeviceId(DEVICE_ID);
            verify(cartRepositoryPort).save(any(Cart.class));
        }
    }

    @Nested
    @DisplayName("findCartByDeviceId()")
    class FindCartByDeviceId {

        private Cart cart;
        private CartItem item;
        private ProductVariant variant;

        @BeforeEach
        void setUp() {
            cart = Cart.builder().id(1L).deviceId(DEVICE_ID).build();
            item = CartItem.builder().id(1L).productVariantId(5L).quantity(2).build();
            variant = ProductVariant.builder().id(5L).stock(10).productId(2L).build();
        }

        @Test
        void shouldReturnCartWithEnrichedItems() {
            when(cartRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(cart));
            when(cartItemRepositoryPort.findAllByCartId(1L)).thenReturn(List.of(item));
            when(productVariantRepositoryPort.findAllByIdInSet(Set.of(5L))).thenReturn(Set.of(variant));

            Cart result = cartService.findCartByDeviceId(DEVICE_ID);

            assertThat(result.getCartItems()).hasSize(1);
            assertThat(result.getCartItems().get(0).getCurrentStock()).isEqualTo(10);
            assertThat(result.getCartItems().get(0).getProductId()).isEqualTo(2L);
        }

        @Test
        void shouldThrow_whenCartNotFound() {
            when(cartRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cartService.findCartByDeviceId(DEVICE_ID))
                    .isInstanceOf(CartNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getCartUpdatedAt()")
    class GetCartUpdatedAt {

        @Test
        void shouldReturnCartUpdatedAt() {
            Instant updatedAt = Instant.now();
            Cart cart = Cart.builder().id(1L).deviceId(DEVICE_ID).updatedAt(updatedAt).build();
            when(cartRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(cart));

            Instant result = cartService.getCartUpdatedAt(DEVICE_ID);

            assertThat(result).isEqualTo(updatedAt);
        }

        @Test
        void shouldThrow_whenCartNotFound() {
            when(cartRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cartService.getCartUpdatedAt(DEVICE_ID))
                    .isInstanceOf(CartNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getCartWithAllChecks()")
    class GetCartWithAllChecks {

        private Cart cart;
        private CartItem item;
        private ProductVariant activeVariant;
        private Product product;

        @BeforeEach
        void setUp() {
            cart = Cart.builder().id(1L).deviceId(DEVICE_ID).build();
            item = CartItem.builder().id(1L).productVariantId(5L).quantity(2).unitPrice(10.0).build();
            activeVariant = ProductVariant.builder().id(5L).stock(10).active(true).productId(2L).build();
            product = Product.builder().id(2L).price(10.0).build();

            when(cartRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(cart));
            when(cartItemRepositoryPort.findAllByCartId(1L)).thenReturn(List.of(item));
            when(productVariantRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(activeVariant));
            when(productRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(product));
        }

        @Test
        void shouldReturnEmptyAlerts_whenNoIssuesDetected() {
            CartWithAllChecks result = cartService.getCartWithAllChecks(DEVICE_ID);

            assertThat(result.changedPrices()).isEmpty();
            assertThat(result.insufficientStock()).isEmpty();
            assertThat(result.unavailableVariants()).isEmpty();
            verify(cartItemRepositoryPort, never()).saveAll(any());
        }

        @Test
        void shouldClampQuantityAndReportAlert_whenStockInsufficient() {
            // item requests 5 but only 3 in stock
            item.setQuantity(5);
            activeVariant = ProductVariant.builder().id(5L).stock(3).active(true).productId(2L).build();
            when(productVariantRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(activeVariant));
            when(cartItemRepositoryPort.saveAll(any())).thenReturn(List.of(item));

            CartWithAllChecks result = cartService.getCartWithAllChecks(DEVICE_ID);

            assertThat(result.insufficientStock()).hasSize(1);
            assertThat(result.insufficientStock().get(0).productVariantId()).isEqualTo(5L);
            assertThat(result.insufficientStock().get(0).availableStock()).isEqualTo(3);
            assertThat(item.getQuantity()).isEqualTo(3);
        }

        @Test
        void shouldRemoveItemAndReportAlert_whenVariantIsInactive() {
            ProductVariant inactiveVariant = ProductVariant.builder().id(5L).stock(10).active(false).productId(2L).build();
            when(productVariantRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(inactiveVariant));
            when(cartItemRepositoryPort.saveAll(any())).thenReturn(List.of());
            when(productRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of());

            CartWithAllChecks result = cartService.getCartWithAllChecks(DEVICE_ID);

            assertThat(result.unavailableVariants()).hasSize(1);
            assertThat(result.unavailableVariants().get(0).productVariantId()).isEqualTo(5L);
            verify(cartItemRepositoryPort).deleteAllByIdIn(List.of(1L));
        }

        @Test
        void shouldUpdatePriceAndReportAlert_whenProductPriceChanged() {
            Product updatedProduct = Product.builder().id(2L).price(15.0).build(); // price changed from 10 to 15
            when(productRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(updatedProduct));
            when(cartItemRepositoryPort.saveAll(any())).thenReturn(List.of(item));

            CartWithAllChecks result = cartService.getCartWithAllChecks(DEVICE_ID);

            assertThat(result.changedPrices()).hasSize(1);
            assertThat(result.changedPrices().get(0).oldPrice()).isEqualTo(10.0);
            assertThat(result.changedPrices().get(0).newPrice()).isEqualTo(15.0);
            assertThat(item.getUnitPrice()).isEqualTo(15.0);
        }
    }

    @Nested
    @DisplayName("updateCartItemsQuantities()")
    class UpdateCartItemsQuantities {

        private Cart cart;
        private CartItem item;
        private ProductVariant variant;

        @BeforeEach
        void setUp() {
            cart = Cart.builder().id(1L).deviceId(DEVICE_ID).build();
            item = CartItem.builder().id(1L).productVariantId(5L).quantity(2).build();
            variant = ProductVariant.builder().id(5L).stock(10).productId(2L).build();

            when(cartRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(cart));
            when(cartItemRepositoryPort.findAllByCartId(1L)).thenReturn(List.of(item));
            when(productVariantRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(variant));
            when(cartRepositoryPort.save(any())).thenReturn(cart);
        }

        @Test
        void shouldUpdateQuantityForMatchingCartItem() {
            CartItemsQuantitiesUpdateDTO dto = CartItemsQuantitiesUpdateDTO.builder()
                    .deviceId(DEVICE_ID)
                    .cartItemCommands(List.of(new UpdateCartItemQuantityCommand(1L, 7)))
                    .build();

            cartService.updateCartItemsQuantities(DEVICE_ID, dto);

            assertThat(item.getQuantity()).isEqualTo(7);
            verify(cartItemRepositoryPort).saveAll(any());
        }

        @Test
        void shouldNotSaveItems_whenCommandListIsEmpty() {
            CartItemsQuantitiesUpdateDTO dto = CartItemsQuantitiesUpdateDTO.builder()
                    .deviceId(DEVICE_ID)
                    .cartItemCommands(List.of())
                    .build();

            cartService.updateCartItemsQuantities(DEVICE_ID, dto);
            assertThat(item.getQuantity()).isEqualTo(2);
            verify(cartItemRepositoryPort, never()).saveAll(any());
        }
    }
}
