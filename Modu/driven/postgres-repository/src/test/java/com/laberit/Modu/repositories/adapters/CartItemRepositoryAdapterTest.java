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
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
        void findByIdAndCartId_returnsMappedCartItem_whenEntityExistsAndReferencesCorrectCartId() {
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
        void findByIdAndCartId_returnsEmpty_whenEntityNotFound() {
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
        void findByCartIdAndProductVariantId_returnsMappedCartItem_whenEntityExists() {
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
        void findByCartIdAndProductVariantId_returnsEmpty_whenEntityNotFound() {
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

    @Nested
    @DisplayName("findAllByCartId() tests")
    class FindAllByCartIdTests {
        private Long cartId;
        private CartItemEntity mockEntity;
        private CartItem mappedCartItem;

        @BeforeEach
        void setUp() {
            cartId = 10L;
            mockEntity = new CartItemEntity();
            mockEntity.setId(1L);
            mappedCartItem = CartItem.builder()
                    .id(1L)
                    .cartId(cartId)
                    .productVariantId(20L)
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();
        }

        @Test
        @DisplayName("Returns the mapped CartItem list when entities exist for the given CartId")
        void findAllByCartId_returnsMappedList_whenEntitiesExist() {
            List<CartItemEntity> entityList = List.of(mockEntity);
            List<CartItem> domainList = List.of(mappedCartItem);

            when(cartItemJpaRepository.findAllByCartId(cartId)).thenReturn(entityList);
            when(cartItemMapper.toDomainList(entityList)).thenReturn(domainList);

            List<CartItem> result = cartItemRepositoryAdapter.findAllByCartId(cartId);

            assertEquals(domainList, result);
            verify(cartItemJpaRepository).findAllByCartId(cartId);
            verify(cartItemMapper).toDomainList(entityList);
        }

        @Test
        @DisplayName("Returns an empty list when no entities exist for the given CartId")
        void findAllByCartId_returnsEmptyList_whenNoEntitiesExist() {
            List<CartItemEntity> emptyEntityList = List.of();
            List<CartItem> emptyDomainList = List.of();

            when(cartItemJpaRepository.findAllByCartId(cartId)).thenReturn(emptyEntityList);
            when(cartItemMapper.toDomainList(emptyEntityList)).thenReturn(emptyDomainList);

            List<CartItem> result = cartItemRepositoryAdapter.findAllByCartId(cartId);

            assertTrue(result.isEmpty());
            verify(cartItemJpaRepository).findAllByCartId(cartId);
            verify(cartItemMapper).toDomainList(emptyEntityList);
        }
    }

    @Nested
    @DisplayName("deleteById() tests")
    class DeleteByIdTests {
        private Long id;

        @BeforeEach
        void setUp() {
            id = 1L;
        }

        @Test
        @DisplayName("Deletes the CartItem by id then flushes")
        void deleteById_deletesEntityAndFlushes() {
            InOrder inOrder = inOrder(cartItemJpaRepository);

            cartItemRepositoryAdapter.deleteById(id);

            inOrder.verify(cartItemJpaRepository).deleteById(id);
            inOrder.verify(cartItemJpaRepository).flush();
        }
    }

    @Nested
    @DisplayName("deleteAllByCartId() tests")
    class DeleteAllByCartIdTests {
        private Long cartId;

        @BeforeEach
        void setUp() {
            cartId = 10L;
        }

        @Test
        @DisplayName("Deletes all CartItems for the given CartId then flushes")
        void deleteAllByCartId_deletesAllEntitiesAndFlushes() {
            InOrder inOrder = inOrder(cartItemJpaRepository);

            cartItemRepositoryAdapter.deleteAllByCartId(cartId);

            inOrder.verify(cartItemJpaRepository).deleteAllByCartId(cartId);
            inOrder.verify(cartItemJpaRepository).flush();
        }
    }

    @Nested
    @DisplayName("deleteAllByIdIn() tests")
    class DeleteAllByIdInTests {
        private List<Long> ids;

        @BeforeEach
        void setUp() {
            ids = List.of(1L, 2L, 3L);
        }

        @Test
        @DisplayName("Deletes all CartItems matching the given id list then flushes")
        void deleteAllByIdIn_deletesAllEntitiesAndFlushes() {
            InOrder inOrder = inOrder(cartItemJpaRepository);

            cartItemRepositoryAdapter.deleteAllByIdIn(ids);

            inOrder.verify(cartItemJpaRepository).deleteAllByIdIn(ids);
            inOrder.verify(cartItemJpaRepository).flush();
        }
    }

    @Nested
    @DisplayName("save() tests")
    class SaveTests {
        private CartItem cartItem;
        private CartItemEntity inputEntity;
        private CartItemEntity savedEntity;
        private CartItem mappedCartItem;

        @BeforeEach
        void setUp() {
            cartItem = CartItem.builder()
                    .cartId(10L)
                    .productVariantId(20L)
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();
            inputEntity = new CartItemEntity();
            savedEntity = new CartItemEntity();
            savedEntity.setId(1L);
            mappedCartItem = CartItem.builder()
                    .id(1L)
                    .cartId(10L)
                    .productVariantId(20L)
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();
        }

        @Test
        @DisplayName("Converts CartItem to entity, persists it, converts back to domain and returns the result")
        void save_persistsAndReturnsMappedCartItem() {
            when(cartItemMapper.toEntity(cartItem)).thenReturn(inputEntity);
            when(cartItemJpaRepository.save(inputEntity)).thenReturn(savedEntity);
            when(cartItemMapper.toDomain(savedEntity)).thenReturn(mappedCartItem);

            CartItem result = cartItemRepositoryAdapter.save(cartItem);

            assertEquals(mappedCartItem, result);
            verify(cartItemMapper).toEntity(cartItem);
            verify(cartItemJpaRepository).save(inputEntity);
            verify(cartItemMapper).toDomain(savedEntity);
        }
    }

    @Nested
    @DisplayName("saveAll() tests")
    class SaveAllTests {
        private List<CartItem> cartItems;
        private List<CartItemEntity> entityList;
        private List<CartItemEntity> savedEntityList;
        private List<CartItem> mappedCartItems;

        @BeforeEach
        void setUp() {
            CartItem cartItem = CartItem.builder()
                    .cartId(10L)
                    .productVariantId(20L)
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();
            CartItemEntity entity = new CartItemEntity();
            CartItemEntity savedEntity = new CartItemEntity();
            savedEntity.setId(1L);
            CartItem mappedCartItem = CartItem.builder()
                    .id(1L)
                    .cartId(10L)
                    .productVariantId(20L)
                    .unitPrice(29.99)
                    .quantity(2)
                    .build();

            cartItems = List.of(cartItem);
            entityList = List.of(entity);
            savedEntityList = List.of(savedEntity);
            mappedCartItems = List.of(mappedCartItem);
        }

        @Test
        @DisplayName("Converts CartItem list to entities, persists all, flushes, converts back to domain list and returns the result")
        void saveAll_persistsAllAndReturnsMappedCartItemList() {
            InOrder inOrder = inOrder(cartItemJpaRepository);

            when(cartItemMapper.toEntityList(cartItems)).thenReturn(entityList);
            when(cartItemJpaRepository.saveAll(entityList)).thenReturn(savedEntityList);
            when(cartItemMapper.toDomainList(savedEntityList)).thenReturn(mappedCartItems);

            List<CartItem> result = cartItemRepositoryAdapter.saveAll(cartItems);

            assertEquals(mappedCartItems, result);
            verify(cartItemMapper).toEntityList(cartItems);
            inOrder.verify(cartItemJpaRepository).saveAll(entityList);
            inOrder.verify(cartItemJpaRepository).flush();
            verify(cartItemMapper).toDomainList(savedEntityList);
        }
    }
}