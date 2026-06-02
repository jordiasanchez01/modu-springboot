package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.repositories.models.CartItemEntity;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CartItemPersistanceMapperTest {

    private CartItemPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CartItemPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenCartItemIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapAllFields() {
            CartItem cartItem = CartItem.builder()
                    .id(1L)
                    .cartId(10L)
                    .productVariantId(5L)
                    .unitPrice(10.00)
                    .quantity(3)
                    .build();

            CartItemEntity entity = mapper.toEntity(cartItem);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getCartId()).isEqualTo(10L);
            assertThat(entity.getUnitPrice()).isEqualTo(10.00);
            assertThat(entity.getQuantity()).isEqualTo(3);
            assertThat(entity.getTotalPrice()).isEqualTo(30.00);
            assertThat(entity.getProductVariant()).isNotNull();
            assertThat(entity.getProductVariant().getId()).isEqualTo(5L);
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
        void shouldMapAllFields() {
            ProductVariantEntity productVariant = new ProductVariantEntity();
            productVariant.setId(5L);

            CartItemEntity entity = new CartItemEntity();
            entity.setId(1L);
            entity.setCartId(10L);
            entity.setProductVariant(productVariant);
            entity.setUnitPrice(10.00);
            entity.setQuantity(3);

            CartItem cartItem = mapper.toDomain(entity);

            assertThat(cartItem.getId()).isEqualTo(1L);
            assertThat(cartItem.getCartId()).isEqualTo(10L);
            assertThat(cartItem.getProductVariantId()).isEqualTo(5L);
            assertThat(cartItem.getUnitPrice()).isEqualTo(10.00);
            assertThat(cartItem.getQuantity()).isEqualTo(3);
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
            ProductVariantEntity variant1 = new ProductVariantEntity();
            variant1.setId(5L);
            CartItemEntity entity1 = new CartItemEntity();
            entity1.setId(1L);
            entity1.setCartId(10L);
            entity1.setProductVariant(variant1);
            entity1.setUnitPrice(10.00);
            entity1.setQuantity(2);

            ProductVariantEntity variant2 = new ProductVariantEntity();
            variant2.setId(6L);
            CartItemEntity entity2 = new CartItemEntity();
            entity2.setId(2L);
            entity2.setCartId(10L);
            entity2.setProductVariant(variant2);
            entity2.setUnitPrice(15.00);
            entity2.setQuantity(1);

            List<CartItem> result = mapper.toDomainList(List.of(entity1, entity2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(0).getProductVariantId()).isEqualTo(5L);
            assertThat(result.get(1).getId()).isEqualTo(2L);
            assertThat(result.get(1).getProductVariantId()).isEqualTo(6L);
        }
    }

    @Nested
    @DisplayName("toEntityList")
    class ToEntityList {

        @Test
        void shouldReturnNull_whenListIsNull() {
            assertThat(mapper.toEntityList(null)).isNull();
        }

        @Test
        void shouldMapEachCartItem() {
            CartItem item1 = CartItem.builder()
                    .id(1L).cartId(10L).productVariantId(5L)
                    .unitPrice(10.00).quantity(2).build();
            CartItem item2 = CartItem.builder()
                    .id(2L).cartId(10L).productVariantId(6L)
                    .unitPrice(15.00).quantity(1).build();

            List<CartItemEntity> result = mapper.toEntityList(List.of(item1, item2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(0).getProductVariant().getId()).isEqualTo(5L);
            assertThat(result.get(1).getId()).isEqualTo(2L);
            assertThat(result.get(1).getProductVariant().getId()).isEqualTo(6L);
        }
    }
}
