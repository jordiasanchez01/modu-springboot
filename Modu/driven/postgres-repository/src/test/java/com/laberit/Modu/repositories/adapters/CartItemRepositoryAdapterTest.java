package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.repositories.CartItemJpaRepository;
import com.laberit.Modu.repositories.mappers.CartItemPersistanceMapper;
import com.laberit.Modu.repositories.models.CartItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemRepositoryAdapterTest {

    @Mock private CartItemJpaRepository cartItemJpaRepository;
    @Mock private CartItemPersistanceMapper cartItemMapper;

    @InjectMocks
    private CartItemRepositoryAdapter cartItemRepositoryAdapter;

    @Nested
    @DisplayName("findById() tests")
    class FindByIdTests {
        private Long id;
        private CartItemEntity mockEntity;
        private CartItem mappedCartItem;

        @BeforeEach
        void setUp() {
            id = 1L;
            mockEntity = new CartItemEntity();
            mockEntity.setId(id);
            mappedCartItem = CartItem.builder()
                    .id(id)
                    .cartId(10L)
                    .productVariantId(20L)
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();
        }

        @Test
        @DisplayName("Returns the mapped CartItem wrapped in Optional when entity exists")
        void findById_returnsMappedCartItem_whenEntityExists() {
            when(cartItemJpaRepository.findById(id)).thenReturn(Optional.of(mockEntity));
            when(cartItemMapper.toDomain(mockEntity)).thenReturn(mappedCartItem);

            Optional<CartItem> result = cartItemRepositoryAdapter.findById(id);

            assertTrue(result.isPresent());
            assertEquals(mappedCartItem, result.get());
            verify(cartItemJpaRepository).findById(id);
            verify(cartItemMapper).toDomain(mockEntity);
        }

        @Test
        @DisplayName("Returns empty Optional when entity not found")
        void findById_returnsEmpty_whenEntityNotFound() {
            when(cartItemJpaRepository.findById(id)).thenReturn(Optional.empty());

            Optional<CartItem> result = cartItemRepositoryAdapter.findById(id);

            assertTrue(result.isEmpty());
            verify(cartItemJpaRepository).findById(id);
            verifyNoInteractions(cartItemMapper);
        }
    }

    @Nested
    @DisplayName("findByIdAndCartId() tests")
    class FindByIdAndCartIdTests {
        private Long id;
        private CartItemEntity mockEntity;
        private CartItem mappedCartItem;
        private Cart mockCart;

        @BeforeEach
        void setUp() {
            id = 1L;
            mockEntity = new CartItemEntity();
            mockEntity.setId(id);
            mockCart = Cart.builder()
                    .id(1L)
                    .build();
            mappedCartItem = CartItem.builder()
                    .id(id)
                    .cartId(mockCart.getId())
                    .productVariantId(20L)
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();

        }

        @Test
        @DisplayName("Returns the mapped CartItem wrapped in Optional when entity exists, and references correct CartId")
        void findById_returnsMappedCartItem_whenEntityExistsAndReferencesCorrectCartId() {
            when(cartItemJpaRepository.findByIdAndCartId(id, mockCart.getId())).thenReturn(Optional.of(mockEntity));
            when(cartItemMapper.toDomain(mockEntity)).thenReturn(mappedCartItem);

            Optional<CartItem> result = cartItemRepositoryAdapter.findByIdAndCartId(id, mockCart.getId());

            assertTrue(result.isPresent());
            assertEquals(mappedCartItem, result.get());
            verify(cartItemJpaRepository).findByIdAndCartId(id, mockCart.getId());
            verify(cartItemMapper).toDomain(mockEntity);
        }

        @Test
        @DisplayName("Returns empty Optional when entity not found")
        void findById_returnsEmpty_whenEntityNotFound() {
            when(cartItemJpaRepository.findByIdAndCartId(id, mockCart.getId())).thenReturn(Optional.empty());

            Optional<CartItem> result = cartItemRepositoryAdapter.findByIdAndCartId(id, mockCart.getId());

            assertTrue(result.isEmpty());
            verify(cartItemJpaRepository).findByIdAndCartId(id, mockCart.getId());
            verifyNoInteractions(cartItemMapper);
        }
    }

    @Nested
    @DisplayName("findByCartIdAndProductVariantId() tests")
    class FindByCartIdAndProductVariantIdTests {
        private Long id;
        private CartItemEntity mockEntity;
        private CartItem mappedCartItem;
        private Cart mockCart;
        private ProductVariant mockProductVariant;

        @BeforeEach
        void setUp() {
            id = 1L;
            mockEntity = new CartItemEntity();
            mockEntity.setId(id);
            mockCart = Cart.builder()
                    .id(1L)
                    .build();
            mockProductVariant = ProductVariant.builder()
                    .id(20L)
                    .build();
            mappedCartItem = CartItem.builder()
                    .id(id)
                    .cartId(mockCart.getId())
                    .productVariantId(mockProductVariant.getId())
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();

        }

        @Test
        @DisplayName("Returns the mapped CartItem wrapped in Optional when entity exists, and references existing ProductVariantId")
        void findById_returnsMappedCartItem_whenEntityExistsAndReferencesCorrectCartId() {
            when(cartItemJpaRepository.findByCartIdAndProductVariantId(
                    mockCart.getId(), mockProductVariant.getId())).thenReturn(Optional.of(mockEntity));
            when(cartItemMapper.toDomain(mockEntity)).thenReturn(mappedCartItem);

            Optional<CartItem> result = cartItemRepositoryAdapter.findByCartIdAndProductVariantId(
                    mockCart.getId(), mockProductVariant.getId());

            assertTrue(result.isPresent());
            assertEquals(mappedCartItem, result.get());
            verify(cartItemJpaRepository).findByCartIdAndProductVariantId(
                    mockCart.getId(), mockProductVariant.getId());
            verify(cartItemMapper).toDomain(mockEntity);
        }

        @Test
        @DisplayName("Returns empty Optional when entity not found")
        void findById_returnsEmpty_whenEntityNotFound() {
            when(cartItemJpaRepository.findByCartIdAndProductVariantId(
                    mockCart.getId(), mockProductVariant.getId())).thenReturn(Optional.empty());

            Optional<CartItem> result = cartItemRepositoryAdapter.findByCartIdAndProductVariantId(
                    mockCart.getId(), mockProductVariant.getId());

            assertTrue(result.isEmpty());
            verify(cartItemJpaRepository).findByCartIdAndProductVariantId(
                    mockCart.getId(), mockProductVariant.getId());
            verifyNoInteractions(cartItemMapper);
        }
    }

    @Test
    void findAllByCartId() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void deleteAllByCartId() {
    }

    @Test
    void deleteAllByIdIn() {
    }

    @Test
    void save() {
    }

    @Test
    void saveAll() {
    }
}