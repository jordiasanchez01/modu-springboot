package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.repositories.CartItemJpaRepository;
import com.laberit.Modu.repositories.CartJpaRepository;
import com.laberit.Modu.repositories.mappers.CartItemPersistanceMapper;
import com.laberit.Modu.repositories.mappers.CartPersistanceMapper;
import com.laberit.Modu.repositories.models.CartEntity;
import com.laberit.Modu.repositories.models.CartItemEntity;
import jakarta.persistence.EntityManager;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartRepositoryAdapterTest {

    @Mock private CartJpaRepository cartJpaRepository;
    @Mock private CartItemJpaRepository cartItemJpaRepository;
    @Mock private CartPersistanceMapper cartMapper;
    @Mock private CartItemPersistanceMapper cartItemMapper;
    @Mock private EntityManager entityManager;

    @InjectMocks
    private CartRepositoryAdapter adapter;

    private CartEntity cartEntity;
    private CartItemEntity cartItemEntity;
    private Cart domainCart;
    private CartItem domainCartItem;

    @BeforeEach
    void setUp() {
        cartItemEntity = new CartItemEntity();
        cartItemEntity.setId(10L);
        cartItemEntity.setCartId(1L);

        cartEntity = new CartEntity();
        cartEntity.setId(1L);
        cartEntity.setDeviceId("1234567890DEVICE1234");
        cartEntity.setCartItems(List.of(cartItemEntity));

        domainCartItem = CartItem.builder()
                .id(10L).cartId(1L).productVariantId(5L).unitPrice(15.0).quantity(1).build();

        domainCart = Cart.builder()
                .id(1L).deviceId("1234567890DEVICE1234")
                .cartItems(List.of(domainCartItem)).build();
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        void shouldSaveCartAndItems_thenRefreshAndReturnDomain() {
            when(cartMapper.toEntity(domainCart)).thenReturn(cartEntity);
            when(cartJpaRepository.save(cartEntity)).thenReturn(cartEntity);
            when(cartItemMapper.toEntityList(domainCart.getCartItems())).thenReturn(List.of(cartItemEntity));
            when(cartMapper.toDomain(cartEntity)).thenReturn(domainCart);
            when(cartItemMapper.toDomainList(cartEntity.getCartItems())).thenReturn(List.of(domainCartItem));

            Cart result = adapter.save(domainCart);

            assertThat(result.getDeviceId()).isEqualTo("device-abc");
            assertThat(result.getCartItems()).hasSize(1);
            verify(cartJpaRepository).save(cartEntity);
            verify(cartItemJpaRepository).saveAll(anyList());
            verify(entityManager).flush();
            verify(entityManager).refresh(cartEntity);
        }

        @Test
        void shouldAssignCartIdToEachItem_beforeSavingItems() {
            CartItemEntity itemWithoutCartId = new CartItemEntity();
            cartEntity.setCartItems(List.of(itemWithoutCartId));

            when(cartMapper.toEntity(domainCart)).thenReturn(cartEntity);
            when(cartJpaRepository.save(cartEntity)).thenReturn(cartEntity);
            when(cartItemMapper.toEntityList(domainCart.getCartItems())).thenReturn(List.of(itemWithoutCartId));
            when(cartMapper.toDomain(cartEntity)).thenReturn(domainCart);
            when(cartItemMapper.toDomainList(cartEntity.getCartItems())).thenReturn(List.of(domainCartItem));

            adapter.save(domainCart);

            assertThat(itemWithoutCartId.getCartId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("deleteByDeviceId()")
    class DeleteByDeviceId {

        @Test
        void shouldCallDeleteThenFlush_inOrder() {
            adapter.deleteByDeviceId("device-abc");

            var inOrder = inOrder(cartJpaRepository);
            inOrder.verify(cartJpaRepository).deleteByDeviceId("device-abc");
            inOrder.verify(cartJpaRepository).flush();
        }
    }

    @Nested
    @DisplayName("findByDeviceId()")
    class FindByDeviceId {

        @Test
        void shouldRefresh_mapCart_andPopulateItems_whenFound() {
            when(cartJpaRepository.findByDeviceId("device-abc")).thenReturn(Optional.of(cartEntity));
            when(cartMapper.toDomain(cartEntity)).thenReturn(domainCart);
            when(cartItemMapper.toDomainList(cartEntity.getCartItems())).thenReturn(List.of(domainCartItem));

            Optional<Cart> result = adapter.findByDeviceId("device-abc");

            assertThat(result).isPresent();
            assertThat(result.get().getDeviceId()).isEqualTo("device-abc");
            assertThat(result.get().getCartItems()).hasSize(1);
            verify(entityManager).refresh(cartEntity);
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(cartJpaRepository.findByDeviceId("unknown")).thenReturn(Optional.empty());

            Optional<Cart> result = adapter.findByDeviceId("unknown");

            assertThat(result).isEmpty();
            verifyNoInteractions(cartMapper);
            verifyNoInteractions(entityManager);
        }
    }

    @Nested
    @DisplayName("existsByDeviceId()")
    class ExistsByDeviceId {

        @Test
        void shouldReturnTrue_whenExists() {
            when(cartJpaRepository.existsByDeviceId("device-abc")).thenReturn(true);
            assertThat(adapter.existsByDeviceId("device-abc")).isTrue();
        }

        @Test
        void shouldReturnFalse_whenNotExists() {
            when(cartJpaRepository.existsByDeviceId("unknown")).thenReturn(false);
            assertThat(adapter.existsByDeviceId("unknown")).isFalse();
        }
    }
}
