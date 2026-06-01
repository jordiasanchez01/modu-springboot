package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.ProductVariantNotAvailableException;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemServiceUseCaseTest {

    @Mock private CartItemRepositoryPort cartItemRepositoryPort;
    @Mock private ProductVariantServicePort productVariantServicePort;
    @Mock private CartRepositoryPort cartRepositoryPort;
    @Mock private ProductVariantRepositoryPort productVariantRepositoryPort;
    @Mock private ProductRepositoryPort productRepositoryPort;

    @InjectMocks
    private CartItemServiceUseCase cartItemServiceUseCase;

    private String deviceId;
    private UpdateCartItemQuantityCommand command;
    private Cart mockCart;
    private CartItem mockCartItem;
    private ProductVariant mockProductVariant;

    @BeforeEach
    void setUp() {
        deviceId = "1000000000000001";
        command = new UpdateCartItemQuantityCommand(1L, 3);
        mockCart = Cart.builder()
            .id(Long.valueOf(deviceId))
            .build();
        mockProductVariant = ProductVariant.builder()
            .id(1L)
            .stock(5)
            .active(true)
            .build();
        mockCartItem = CartItem.builder()
            .id(command.cartItemId())
            .cartId(mockCart.getId())
            .productVariantId(mockProductVariant.getId())
            .quantity(2)
            .build();
    }


    @Test
    void updateCartItemQuantity_happyPath() {

        when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));
        when(cartItemRepositoryPort.findByIdAndCartId(
                command.cartItemId(), mockCart.getId())).thenReturn(Optional.of(mockCartItem));
        when(productVariantRepositoryPort.findById(mockCartItem.getProductVariantId())).thenReturn(Optional.of(mockProductVariant));
        when(cartItemRepositoryPort.save(any())).thenReturn(mockCartItem);

        // When
        CartItem result = cartItemServiceUseCase.updateCartItemQuantity(deviceId, command);

        // Then
        verify(cartRepositoryPort).findByDeviceId(deviceId);
        verify(cartItemRepositoryPort).findByIdAndCartId(command.cartItemId(), mockCart.getId());
        verify(productVariantRepositoryPort).findById(mockCartItem.getProductVariantId());
        verify(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());
        verify(cartItemRepositoryPort, times(2)).save(any());
    }

    @Test
    void deleteCartItemById() {
    }

    @Test
    void deleteAllCartItems() {
    }

    @Test
    void addCartItemToCart() {
    }
}