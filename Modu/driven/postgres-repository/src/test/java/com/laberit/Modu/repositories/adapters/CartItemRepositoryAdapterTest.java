package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.repositories.CartItemJpaRepository;
import com.laberit.Modu.repositories.mappers.CartItemPersistanceMapper;
import com.laberit.Modu.repositories.models.CartItemEntity;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemRepositoryAdapterTest {

    @Mock private CartItemJpaRepository cartItemJpaRepository;
    @Mock private CartItemPersistanceMapper cartItemMapper;

    @InjectMocks
    private CartItemRepositoryAdapter adapter;

    private CartItem domainItem;
    private CartItemEntity entityItem;

    @BeforeEach
    void setUp() {
        ProductVariantEntity variant = new ProductVariantEntity();
        variant.setId(5L);

        entityItem = new CartItemEntity();
        entityItem.setId(1L);
        entityItem.setCartId(10L);
        entityItem.setProductVariant(variant);
        entityItem.setUnitPrice(10.0);
        entityItem.setQuantity(2);

        domainItem = CartItem.builder()
                .id(1L).cartId(10L).productVariantId(5L)
                .unitPrice(10.0).quantity(2).build();
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        void shouldReturnMappedDomain_whenEntityFound() {
            when(cartItemJpaRepository.findById(1L)).thenReturn(Optional.of(entityItem));
            when(cartItemMapper.toDomain(entityItem)).thenReturn(domainItem);

            Optional<CartItem> result = adapter.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            verify(cartItemMapper).toDomain(entityItem);
        }

        @Test
        void shouldReturnEmpty_whenEntityNotFound() {
            when(cartItemJpaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<CartItem> result = adapter.findById(99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(cartItemMapper);
        }
    }

    @Nested
    @DisplayName("findByIdAndCartId()")
    class FindByIdAndCartId {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(cartItemJpaRepository.findByIdAndCartId(1L, 10L)).thenReturn(Optional.of(entityItem));
            when(cartItemMapper.toDomain(entityItem)).thenReturn(domainItem);

            Optional<CartItem> result = adapter.findByIdAndCartId(1L, 10L);

            assertThat(result).isPresent();
            assertThat(result.get().getCartId()).isEqualTo(10L);
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(cartItemJpaRepository.findByIdAndCartId(1L, 99L)).thenReturn(Optional.empty());

            Optional<CartItem> result = adapter.findByIdAndCartId(1L, 99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(cartItemMapper);
        }
    }

    @Nested
    @DisplayName("findByCartIdAndProductVariantId()")
    class FindByCartIdAndProductVariantId {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(cartItemJpaRepository.findByCartIdAndProductVariantId(10L, 5L))
                    .thenReturn(Optional.of(entityItem));
            when(cartItemMapper.toDomain(entityItem)).thenReturn(domainItem);

            Optional<CartItem> result = adapter.findByCartIdAndProductVariantId(10L, 5L);

            assertThat(result).isPresent();
            assertThat(result.get().getProductVariantId()).isEqualTo(5L);
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(cartItemJpaRepository.findByCartIdAndProductVariantId(10L, 99L))
                    .thenReturn(Optional.empty());

            Optional<CartItem> result = adapter.findByCartIdAndProductVariantId(10L, 99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(cartItemMapper);
        }
    }

    @Nested
    @DisplayName("findAllByCartId()")
    class FindAllByCartId {

        @Test
        void shouldReturnMappedDomainList() {
            when(cartItemJpaRepository.findAllByCartId(10L)).thenReturn(List.of(entityItem));
            when(cartItemMapper.toDomainList(List.of(entityItem))).thenReturn(List.of(domainItem));

            List<CartItem> result = adapter.findAllByCartId(10L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }

        @Test
        void shouldReturnEmptyList_whenCartHasNoItems() {
            when(cartItemJpaRepository.findAllByCartId(10L)).thenReturn(List.of());
            when(cartItemMapper.toDomainList(List.of())).thenReturn(List.of());

            List<CartItem> result = adapter.findAllByCartId(10L);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("deleteById()")
    class DeleteById {

        @Test
        void shouldCallDeleteThenFlush_inOrder() {
            InOrder inOrder = inOrder(cartItemJpaRepository);

            adapter.deleteById(1L);

            inOrder.verify(cartItemJpaRepository).deleteById(1L);
            inOrder.verify(cartItemJpaRepository).flush();
        }
    }

    @Nested
    @DisplayName("deleteAllByCartId()")
    class DeleteAllByCartId {

        @Test
        void shouldCallDeleteThenFlush_inOrder() {
            InOrder inOrder = inOrder(cartItemJpaRepository);

            adapter.deleteAllByCartId(10L);

            inOrder.verify(cartItemJpaRepository).deleteAllByCartId(10L);
            inOrder.verify(cartItemJpaRepository).flush();
        }
    }

    @Nested
    @DisplayName("deleteAllByIdIn()")
    class DeleteAllByIdIn {

        @Test
        void shouldCallDeleteThenFlush_inOrder() {
            List<Long> ids = List.of(1L, 2L, 3L);
            InOrder inOrder = inOrder(cartItemJpaRepository);

            adapter.deleteAllByIdIn(ids);

            inOrder.verify(cartItemJpaRepository).deleteAllByIdIn(ids);
            inOrder.verify(cartItemJpaRepository).flush();
        }
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        void shouldConvertToEntity_persist_andConvertBack() {
            when(cartItemMapper.toEntity(domainItem)).thenReturn(entityItem);
            when(cartItemJpaRepository.save(entityItem)).thenReturn(entityItem);
            when(cartItemMapper.toDomain(entityItem)).thenReturn(domainItem);

            CartItem result = adapter.save(domainItem);

            assertThat(result).isEqualTo(domainItem);
            InOrder inOrder = inOrder(cartItemMapper, cartItemJpaRepository);
            inOrder.verify(cartItemMapper).toEntity(domainItem);
            inOrder.verify(cartItemJpaRepository).save(entityItem);
            inOrder.verify(cartItemMapper).toDomain(entityItem);
        }
    }

    @Nested
    @DisplayName("saveAll()")
    class SaveAll {

        @Test
        void shouldConvertAll_persistAll_flush_andConvertBack() {
            List<CartItem> domainItems = List.of(domainItem);
            List<CartItemEntity> entityItems = List.of(entityItem);

            when(cartItemMapper.toEntityList(domainItems)).thenReturn(entityItems);
            when(cartItemJpaRepository.saveAll(entityItems)).thenReturn(entityItems);
            when(cartItemMapper.toDomainList(entityItems)).thenReturn(domainItems);

            List<CartItem> result = adapter.saveAll(domainItems);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            InOrder inOrder = inOrder(cartItemJpaRepository);
            inOrder.verify(cartItemJpaRepository).saveAll(entityItems);
            inOrder.verify(cartItemJpaRepository).flush();
        }
    }
}
