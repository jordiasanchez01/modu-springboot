package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartEmptyException;
import com.laberit.Modu.domain.exceptions.OrderNotFoundException;
import com.laberit.Modu.domain.exceptions.OrderNotPaidException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.domain.model.response.CartWithAllChecks;
import com.laberit.Modu.domain.model.response.CheckoutResult;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.ports.driven.OrderRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceUseCaseTest {

    @Mock private OrderRepositoryPort orderRepositoryPort;
    @Mock private CartServicePort cartServicePort;
    @Mock private CartItemServicePort cartItemServicePort;
    @Mock private ProductVariantRepositoryPort productVariantRepositoryPort;

    @InjectMocks
    private OrderServiceUseCase orderService;

    private static final String DEVICE_ID = "1000000000000001";

    @Nested
    @DisplayName("findOrderById()")
    class FindOrderById {

        @Test
        void shouldReturnOrder_whenFound() {
            Order order = Order.builder().id(1L).deviceId(DEVICE_ID).build();
            when(orderRepositoryPort.findByOrderId(1L)).thenReturn(Optional.of(order));

            Order result = orderService.findOrderById(1L);

            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        void shouldThrow_whenOrderNotFound() {
            when(orderRepositoryPort.findByOrderId(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.findOrderById(99L))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("findOrderByDeviceId()")
    class FindOrderByDeviceId {

        @Test
        void shouldReturnOrder_whenFound() {
            Order order = Order.builder().id(1L).deviceId(DEVICE_ID).build();
            when(orderRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.of(order));

            Order result = orderService.findOrderByDeviceId(DEVICE_ID);

            assertThat(result.getDeviceId()).isEqualTo(DEVICE_ID);
        }

        @Test
        void shouldThrow_whenOrderNotFound() {
            when(orderRepositoryPort.findByDeviceId(DEVICE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.findOrderByDeviceId(DEVICE_ID))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("addOrder()")
    class AddOrder {

        private CartItemsQuantitiesUpdateDTO cartToOrderDto;
        private CartItem cartItem;
        private Cart cartWithItems;

        @BeforeEach
        void setUp() {
            cartToOrderDto = CartItemsQuantitiesUpdateDTO.builder()
                    .deviceId(DEVICE_ID)
                    .cartItemCommands(List.of())
                    .build();
            cartItem = CartItem.builder().id(1L).productVariantId(5L)
                    .unitPrice(10.0).quantity(2).productId(2L).build();
            cartWithItems = Cart.builder().id(1L).deviceId(DEVICE_ID)
                    .cartItems(List.of(cartItem)).build();
        }

        // --- validateAddOrderCommand bugs (raw RuntimeException instead of domain exception) ---

        @Test
        @DisplayName("throws RuntimeException when isPaid is null (known bug: should use domain exception)")
        void shouldThrowRuntimeException_whenIsPaidIsNull() {
            AddOrderCommand command = new AddOrderCommand(null, "Leave at door", 0.0, cartToOrderDto);

            assertThatThrownBy(() -> orderService.addOrder(DEVICE_ID, command))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("isPaid");
        }

        @Test
        @DisplayName("throws RuntimeException when specialInstructions is null (known bug: should use domain exception)")
        void shouldThrowRuntimeException_whenSpecialInstructionsIsNull() {
            AddOrderCommand command = new AddOrderCommand(true, null, 0.0, cartToOrderDto);

            assertThatThrownBy(() -> orderService.addOrder(DEVICE_ID, command))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("specialInstructions");
        }

        @Test
        void shouldThrow_whenIsPaidIsFalse() {
            AddOrderCommand command = new AddOrderCommand(false, "Leave at door", 0.0, cartToOrderDto);

            assertThatThrownBy(() -> orderService.addOrder(DEVICE_ID, command))
                    .isInstanceOf(OrderNotPaidException.class);
        }

        @Test
        void shouldPlaceOrderAndDecrementStock_whenCartIsValid() {
            AddOrderCommand command = new AddOrderCommand(true, "Leave at door", 0.0, cartToOrderDto);
            CartWithAllChecks validCart = new CartWithAllChecks(cartWithItems, List.of(), List.of(), List.of());
            Order savedOrder = Order.builder().id(1L).build();

            when(cartServicePort.updateCartItemsQuantities(eq(DEVICE_ID), any())).thenReturn(cartWithItems);
            when(cartServicePort.getCartWithAllChecks(DEVICE_ID)).thenReturn(validCart);
            when(orderRepositoryPort.saveWithoutItems(any())).thenReturn(savedOrder);
            when(orderRepositoryPort.save(any())).thenReturn(savedOrder);
            when(productVariantRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of());
            when(productVariantRepositoryPort.saveAll(any())).thenReturn(List.of());

            CheckoutResult result = orderService.addOrder(DEVICE_ID, command);

            assertThat(result.isOrderPlaced()).isTrue();
            verify(orderRepositoryPort).saveWithoutItems(any());
            verify(orderRepositoryPort).save(any());
            verify(cartItemServicePort).deleteAllCartItems(DEVICE_ID);
        }

        @Test
        void shouldReturnConflictResult_whenCartHasValidationAlerts() {
            AddOrderCommand command = new AddOrderCommand(true, "Leave at door", 0.0, cartToOrderDto);
            InsufficientStockResult stockAlert = new InsufficientStockResult(5L, 5, 3);
            CartWithAllChecks invalidCart = new CartWithAllChecks(
                    cartWithItems, List.of(), List.of(stockAlert), List.of());

            when(cartServicePort.updateCartItemsQuantities(eq(DEVICE_ID), any())).thenReturn(cartWithItems);
            when(cartServicePort.getCartWithAllChecks(DEVICE_ID)).thenReturn(invalidCart);

            CheckoutResult result = orderService.addOrder(DEVICE_ID, command);

            assertThat(result.isOrderPlaced()).isFalse();
            verify(orderRepositoryPort, never()).saveWithoutItems(any());
            verify(cartItemServicePort, never()).deleteAllCartItems(any());
        }

        @Test
        void shouldThrow_whenCartIsEmptyAfterValidation() {
            AddOrderCommand command = new AddOrderCommand(true, "Leave at door", 0.0, cartToOrderDto);
            Cart emptyCart = Cart.builder().id(1L).deviceId(DEVICE_ID).cartItems(List.of()).build();
            CartWithAllChecks emptyCartChecks = new CartWithAllChecks(
                    emptyCart, List.of(), List.of(), List.of());

            when(cartServicePort.updateCartItemsQuantities(eq(DEVICE_ID), any())).thenReturn(emptyCart);
            when(cartServicePort.getCartWithAllChecks(DEVICE_ID)).thenReturn(emptyCartChecks);

            assertThatThrownBy(() -> orderService.addOrder(DEVICE_ID, command))
                    .isInstanceOf(CartEmptyException.class);
        }
    }
}
