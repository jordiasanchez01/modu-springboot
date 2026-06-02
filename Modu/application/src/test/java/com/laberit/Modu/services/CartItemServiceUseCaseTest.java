package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartItemNotFoundException;
import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotAvailableException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
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


    @Nested
    @DisplayName("updateCartItemQuantity() tests")
    class UpdateCartItemQuantityTests {
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
        @DisplayName("If associated Cart, CartItem, and ProductVariant exist, and there is sufficient Stock, update the Cart Item quantity")
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
            verify(cartItemRepositoryPort).save(any());
        }

        @Test
        @DisplayName("CartNotFoundException is thrown when Cart not found")
        void updateCartItemQuantity_shouldThrowCartNotFoundException_whenCartNotFound() {

            // WHEN
            // The ONE thing that fails: the cart doesn't exist for this deviceId
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.empty());
            // THEN
            assertThrows(CartNotFoundException.class, () ->
                    cartItemServiceUseCase.updateCartItemQuantity(deviceId, command)
            );

            // VERIFY the method stopped after step 1 — nothing else was called
            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verifyNoInteractions(cartItemRepositoryPort);
            verifyNoInteractions(productVariantRepositoryPort);
            verifyNoInteractions(productVariantServicePort);
        }

        @Test
        @DisplayName("CartItemNotFoundException is thrown when CartItem not found")
        void updateCartItemQuantity_shouldThrowCartItemNotFoundException_whenItemNotFound() {
            // WHEN
            // Step 1 succeeds, Step 2 fails
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByIdAndCartId(command.cartItemId(), mockCart.getId()))
                    .thenReturn(Optional.empty()); // ← the one failure

            assertThrows(CartItemNotFoundException.class, () ->
                    cartItemServiceUseCase.updateCartItemQuantity(deviceId, command)
            );

            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verify(cartItemRepositoryPort).findByIdAndCartId(command.cartItemId(), mockCart.getId());
            verifyNoInteractions(productVariantRepositoryPort);
            verifyNoInteractions(productVariantServicePort);
        }

        @Test
        @DisplayName("ProductVariantNotFoundException is thrown when ProductVariant not found")
        void updateCartItemQuantity_shouldThrowProductVariantNotFoundException_whenVariantNotFound() {
            // WHEN
            // Steps 1-2 succeed, Step 3 fails
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByIdAndCartId(command.cartItemId(), mockCart.getId()))
                    .thenReturn(Optional.of(mockCartItem));
            when(productVariantRepositoryPort.findById(mockCartItem.getProductVariantId()))
                    .thenReturn(Optional.empty()); // ← the one failure

            assertThrows(ProductVariantNotFoundException.class, () ->
                    cartItemServiceUseCase.updateCartItemQuantity(deviceId, command)
            );

            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verify(cartItemRepositoryPort).findByIdAndCartId(command.cartItemId(), mockCart.getId());
            verify(productVariantRepositoryPort).findById(mockCartItem.getProductVariantId());
            verifyNoInteractions(productVariantServicePort);
        }

        @Test
        @DisplayName("ProductVariantNotAvailableException(NotEnoughStock) is thrown when not enough Stock available to match requested Quantity")
        void updateCartItemQuantity_shouldThrow_whenStockInsufficient() {
            // WHEN
            // Steps 1-3 succeed, Step 4 throws
            mockProductVariant.setStock(3);
            command = new UpdateCartItemQuantityCommand(1L, 4);

            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByIdAndCartId(command.cartItemId(), mockCart.getId()))
                    .thenReturn(Optional.of(mockCartItem));
            when(productVariantRepositoryPort.findById(mockCartItem.getProductVariantId()))
                    .thenReturn(Optional.of(mockProductVariant));
            doThrow(new ProductVariantNotAvailableException(mockProductVariant.getId(), true)) // ← the one failure (use whatever exception this throws)
                    .when(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());

            assertTrue(mockProductVariant.getStock() < command.quantity());
            assertThrows(ProductVariantNotAvailableException.class, () ->
                    cartItemServiceUseCase.updateCartItemQuantity(deviceId, command)
            );
            // Everything up to step 4 ran, but save() never did
            verify(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());
            // Note: findByIdAndCartId IS on cartItemRepositoryPort, so verify that separately:
            verify(cartItemRepositoryPort).findByIdAndCartId(command.cartItemId(), mockCart.getId());
            verify(cartItemRepositoryPort, never()).save(any());
        }

        @Test
        @DisplayName("ProductVariantNotAvailableException(VariantNotActive) is thrown when ProductVariant.active is set to false")
        void updateCartItemQuantity_shouldThrow_whenVariantNotActive() {
            // WHEN
            mockProductVariant.setActive(false);

            // Steps 1-3 succeed, Step 4 throws
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByIdAndCartId(command.cartItemId(), mockCart.getId()))
                    .thenReturn(Optional.of(mockCartItem));
            when(productVariantRepositoryPort.findById(mockCartItem.getProductVariantId()))
                    .thenReturn(Optional.of(mockProductVariant));
            doThrow(new ProductVariantNotAvailableException(mockProductVariant.getId(), false)) // ← the one failure (variant has active field set to false)
                    .when(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());

            assertFalse(mockProductVariant.getActive());
            assertThrows(ProductVariantNotAvailableException.class, () ->
                    cartItemServiceUseCase.updateCartItemQuantity(deviceId, command)
            );
            // Everything up to step 4 ran, but save() never did
            verify(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());
            verify(cartItemRepositoryPort).findByIdAndCartId(command.cartItemId(), mockCart.getId());
            verify(cartItemRepositoryPort, never()).save(any());
        }

    }



    @Nested
    @DisplayName("deleteCartItemById() tests")
    class DeleteCartItemByIdTests {
        private Long itemId;

        @BeforeEach
        void setUp() {
            deviceId = "1000000000000001";
            itemId = 1L;
            mockCart = Cart.builder()
                    .id(Long.valueOf(deviceId))
                    .build();
            mockCartItem = CartItem.builder()
                    .id(itemId)
                    .cartId(mockCart.getId())
                    .build();
        }

        @Test
        @DisplayName("If associated Cart and CartItem exist, delete the CartItem")
        void deleteCartItemById_happyPath() {
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByIdAndCartId(itemId, mockCart.getId())).thenReturn(Optional.of(mockCartItem));

            cartItemServiceUseCase.deleteCartItemById(deviceId, itemId);

            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verify(cartItemRepositoryPort).findByIdAndCartId(itemId, mockCart.getId());
            verify(cartItemRepositoryPort).deleteById(mockCartItem.getId());
        }

        @Test
        @DisplayName("CartNotFoundException is thrown when Cart not found")
        void deleteCartItemById_shouldThrowCartNotFoundException_whenCartNotFound() {
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.empty());

            assertThrows(CartNotFoundException.class, () ->
                    cartItemServiceUseCase.deleteCartItemById(deviceId, itemId)
            );

            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verifyNoInteractions(cartItemRepositoryPort);
        }

        @Test
        @DisplayName("CartItemNotFoundException is thrown when CartItem not found")
        void deleteCartItemById_shouldThrowCartItemNotFoundException_whenItemNotFound() {
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByIdAndCartId(itemId, mockCart.getId())).thenReturn(Optional.empty());

            assertThrows(CartItemNotFoundException.class, () ->
                    cartItemServiceUseCase.deleteCartItemById(deviceId, itemId)
            );

            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verify(cartItemRepositoryPort).findByIdAndCartId(itemId, mockCart.getId());
            verify(cartItemRepositoryPort, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("deleteAllCartItems() tests")
    class DeleteAllCartItemsTests {

        @BeforeEach
        void setUp() {
            deviceId = "1000000000000001";
            mockCart = Cart.builder()
                    .id(Long.valueOf(deviceId))
                    .build();
        }

        @Test
        @DisplayName("If associated Cart exists, delete all its CartItems")
        void deleteAllCartItems_happyPath() {
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.of(mockCart));

            cartItemServiceUseCase.deleteAllCartItems(deviceId);

            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verify(cartItemRepositoryPort).deleteAllByCartId(mockCart.getId());
        }

        @Test
        @DisplayName("CartNotFoundException is thrown when Cart not found")
        void deleteAllCartItems_shouldThrowCartNotFoundException_whenCartNotFound() {
            when(cartRepositoryPort.findByDeviceId(deviceId)).thenReturn(Optional.empty());

            assertThrows(CartNotFoundException.class, () ->
                    cartItemServiceUseCase.deleteAllCartItems(deviceId)
            );

            verify(cartRepositoryPort).findByDeviceId(deviceId);
            verifyNoInteractions(cartItemRepositoryPort);
        }
    }

    @Nested
    @DisplayName("addCartItemToCart() tests")
    class AddCartItemToCartTests {
        private AddCartItemCommand command;
        private Product mockProduct;

        @BeforeEach
        void setUp() {
            deviceId = "1000000000000001";
            mockProductVariant = ProductVariant.builder()
                    .id(1L)
                    .stock(5)
                    .active(true)
                    .productId(10L)
                    .build();
            mockProduct = Product.builder()
                    .id(10L)
                    .price(29.99)
                    .build();
            mockCart = Cart.builder()
                    .id(100L)
                    .deviceId(deviceId)
                    .build();
            mockCartItem = CartItem.builder()
                    .id(1L)
                    .cartId(mockCart.getId())
                    .productVariantId(mockProductVariant.getId())
                    .quantity(2)
                    .unitPrice(mockProduct.getPrice())
                    .currentStock(mockProductVariant.getStock())
                    .build();
            command = new AddCartItemCommand(deviceId, mockProductVariant.getId(), 3);
        }

        @Test
        @DisplayName("If ProductVariant, Product, and Cart exist, stock is sufficient, and no existing CartItem for this variant, save a new CartItem and return the updated Cart")
        void addCartItemToCart_happyPath_newItem() {
            List<CartItem> savedItems = List.of(mockCartItem);

            when(productVariantRepositoryPort.findById(command.productVariantId())).thenReturn(Optional.of(mockProductVariant));
            when(productRepositoryPort.findById(mockProductVariant.getProductId())).thenReturn(Optional.of(mockProduct));
            when(cartRepositoryPort.findByDeviceId(command.deviceId())).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByCartIdAndProductVariantId(mockCart.getId(), command.productVariantId())).thenReturn(Optional.empty());
            when(cartItemRepositoryPort.save(any())).thenReturn(mockCartItem);
            when(cartItemRepositoryPort.findAllByCartId(mockCart.getId())).thenReturn(savedItems);
            when(productVariantRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(mockProductVariant));

            Cart result = cartItemServiceUseCase.addCartItemToCart(command);

            verify(productVariantRepositoryPort).findById(command.productVariantId());
            verify(productRepositoryPort).findById(mockProductVariant.getProductId());
            verify(cartRepositoryPort, times(2)).findByDeviceId(command.deviceId());
            verify(cartItemRepositoryPort).findByCartIdAndProductVariantId(mockCart.getId(), command.productVariantId());
            verify(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());
            verify(cartItemRepositoryPort).save(any());
            verify(cartItemRepositoryPort).findAllByCartId(mockCart.getId());
            verify(productVariantRepositoryPort).findAllByIdInSet(any());
            assertNotNull(result);
        }

        @Test
        @DisplayName("If a CartItem for this variant already exists, increment its quantity and return the updated Cart")
        void addCartItemToCart_happyPath_existingItem() {
            List<CartItem> savedItems = List.of(mockCartItem);
            int expectedTotalQuantity = mockCartItem.getQuantity() + command.quantity();

            when(productVariantRepositoryPort.findById(command.productVariantId())).thenReturn(Optional.of(mockProductVariant));
            when(productRepositoryPort.findById(mockProductVariant.getProductId())).thenReturn(Optional.of(mockProduct));
            when(cartRepositoryPort.findByDeviceId(command.deviceId())).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByCartIdAndProductVariantId(mockCart.getId(), command.productVariantId())).thenReturn(Optional.of(mockCartItem));
            when(cartItemRepositoryPort.save(any())).thenReturn(mockCartItem);
            when(cartItemRepositoryPort.findAllByCartId(mockCart.getId())).thenReturn(savedItems);
            when(productVariantRepositoryPort.findAllByIdInSet(any())).thenReturn(Set.of(mockProductVariant));

            Cart result = cartItemServiceUseCase.addCartItemToCart(command);

            verify(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, expectedTotalQuantity);
            verify(cartItemRepositoryPort).save(mockCartItem);
            assertNotNull(result);
        }

        @Test
        @DisplayName("ProductVariantNotFoundException is thrown when ProductVariant not found")
        void addCartItemToCart_shouldThrowProductVariantNotFoundException_whenVariantNotFound() {
            when(productVariantRepositoryPort.findById(command.productVariantId())).thenReturn(Optional.empty());

            assertThrows(ProductVariantNotFoundException.class, () ->
                    cartItemServiceUseCase.addCartItemToCart(command)
            );

            verify(productVariantRepositoryPort).findById(command.productVariantId());
            verifyNoInteractions(productRepositoryPort);
            verifyNoInteractions(cartRepositoryPort);
            verifyNoInteractions(cartItemRepositoryPort);
            verifyNoInteractions(productVariantServicePort);
        }

        @Test
        @DisplayName("ProductNotFoundException is thrown when Product not found")
        void addCartItemToCart_shouldThrowProductNotFoundException_whenProductNotFound() {
            when(productVariantRepositoryPort.findById(command.productVariantId())).thenReturn(Optional.of(mockProductVariant));
            when(productRepositoryPort.findById(mockProductVariant.getProductId())).thenReturn(Optional.empty());

            assertThrows(ProductNotFoundException.class, () ->
                    cartItemServiceUseCase.addCartItemToCart(command)
            );

            verify(productVariantRepositoryPort).findById(command.productVariantId());
            verify(productRepositoryPort).findById(mockProductVariant.getProductId());
            verifyNoInteractions(cartRepositoryPort);
            verifyNoInteractions(cartItemRepositoryPort);
            verifyNoInteractions(productVariantServicePort);
        }

        @Test
        @DisplayName("CartNotFoundException is thrown when Cart not found")
        void addCartItemToCart_shouldThrowCartNotFoundException_whenCartNotFound() {
            when(productVariantRepositoryPort.findById(command.productVariantId())).thenReturn(Optional.of(mockProductVariant));
            when(productRepositoryPort.findById(mockProductVariant.getProductId())).thenReturn(Optional.of(mockProduct));
            when(cartRepositoryPort.findByDeviceId(command.deviceId())).thenReturn(Optional.empty());

            assertThrows(CartNotFoundException.class, () ->
                    cartItemServiceUseCase.addCartItemToCart(command)
            );

            verify(cartRepositoryPort).findByDeviceId(command.deviceId());
            verifyNoInteractions(cartItemRepositoryPort);
            verifyNoInteractions(productVariantServicePort);
        }

        @Test
        @DisplayName("ProductVariantNotAvailableException is thrown when ProductVariant is not available to purchase")
        void addCartItemToCart_shouldThrowProductVariantNotAvailableException_whenVariantNotAvailable() {
            when(productVariantRepositoryPort.findById(command.productVariantId())).thenReturn(Optional.of(mockProductVariant));
            when(productRepositoryPort.findById(mockProductVariant.getProductId())).thenReturn(Optional.of(mockProduct));
            when(cartRepositoryPort.findByDeviceId(command.deviceId())).thenReturn(Optional.of(mockCart));
            when(cartItemRepositoryPort.findByCartIdAndProductVariantId(mockCart.getId(), command.productVariantId())).thenReturn(Optional.empty());
            doThrow(new ProductVariantNotAvailableException(mockProductVariant.getId(), true))
                    .when(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());

            assertThrows(ProductVariantNotAvailableException.class, () ->
                    cartItemServiceUseCase.addCartItemToCart(command)
            );

            verify(productVariantServicePort).assertIsValidToPurchase(mockProductVariant, command.quantity());
            verify(cartItemRepositoryPort, never()).save(any());
        }
    }
}