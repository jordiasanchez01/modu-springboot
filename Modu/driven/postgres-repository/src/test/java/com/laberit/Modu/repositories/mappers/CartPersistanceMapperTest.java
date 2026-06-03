package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.repositories.models.CartEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartPersistanceMapperTest {

    private CartPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CartPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenCartIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapIdAndDeviceId() {
            Cart cart = Cart.builder().id(1L).deviceId("device-abc").build();

            CartEntity entity = mapper.toEntity(cart);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getDeviceId()).isEqualTo("device-abc");
        }

        @Test
        void shouldNotMapTimestamps() {
            // createdAt and updatedAt are DB-managed (insertable=false, updatable=false)
            // and must not be written by the mapper.
            Cart cart = Cart.builder()
                    .id(1L).deviceId("device-abc")
                    .createdAt(Instant.now()).updatedAt(Instant.now())
                    .build();

            CartEntity entity = mapper.toEntity(cart);

            assertThat(entity.getCreatedAt()).isNull();
            assertThat(entity.getUpdatedAt()).isNull();
        }

        @Test
        void shouldNotMapCartItems() {
            // cartItems are populated separately after save + refresh;
            // toEntity only writes the cart header row.
            Cart cart = Cart.builder().id(1L).deviceId("device-abc").build();

            CartEntity entity = mapper.toEntity(cart);

            assertThat(entity.getCartItems()).isNull();
        }
    }

    @Nested
    @DisplayName("toDomain")
    class ToDomain {

        @Test
        void shouldReturnNull_whenEntityIsNull() {
            assertThat(mapper.toDomain(null)).isNull();
        }

        @Test
        void shouldMapIdDeviceIdAndTimestamps() {
            Instant now = Instant.now();
            CartEntity entity = new CartEntity();
            entity.setId(1L);
            entity.setDeviceId("device-abc");
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);

            Cart cart = mapper.toDomain(entity);

            assertThat(cart.getId()).isEqualTo(1L);
            assertThat(cart.getDeviceId()).isEqualTo("device-abc");
            assertThat(cart.getCreatedAt()).isEqualTo(now);
            assertThat(cart.getUpdatedAt()).isEqualTo(now);
        }

        @Test
        void shouldNotMapCartItems() {
            // cartItems are populated by the adapter after entityManager.refresh();
            // toDomain must not forward the entity's item collection.
            CartEntity entity = new CartEntity();
            entity.setId(1L);
            entity.setDeviceId("device-abc");

            Cart cart = mapper.toDomain(entity);

            assertThat(cart.getCartItems()).isEmpty();
        }
    }

    @Nested
    @DisplayName("toDomainList")
    class ToDomainList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toDomainList(null)).isNull();
        }

        @Test
        void shouldMapEachEntity() {
            CartEntity e1 = new CartEntity();
            e1.setId(1L);
            e1.setDeviceId("device-001");
            CartEntity e2 = new CartEntity();
            e2.setId(2L);
            e2.setDeviceId("device-002");

            List<Cart> result = mapper.toDomainList(List.of(e1, e2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(1).getDeviceId()).isEqualTo("device-002");
        }
    }
}
