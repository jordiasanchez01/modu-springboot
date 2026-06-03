package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.repositories.models.OrderEntity;
import com.laberit.Modu.repositories.models.OrderItemEntity;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemPersistanceMapperTest {

    private OrderItemPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderItemPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenOrderItemIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapPriceAndQuantityFields() {
            OrderItem domain = OrderItem.builder()
                    .id(1L).orderId(10L).productVariantId(5L)
                    .unitPrice(20.0).quantity(3)
                    .build();

            OrderItemEntity entity = mapper.toEntity(domain);

            assertThat(entity.getUnitPrice()).isEqualTo(20.0);
            assertThat(entity.getQuantity()).isEqualTo(3);
        }

        @Test
        void shouldComputeAndStoreTotalPrice() {
            // totalPrice is derived from getTotalPrice() on the domain model (unitPrice * quantity)
            OrderItem domain = OrderItem.builder()
                    .id(1L).orderId(10L).productVariantId(5L)
                    .unitPrice(10.0).quantity(3)
                    .build();

            OrderItemEntity entity = mapper.toEntity(domain);

            assertThat(entity.getTotalPrice()).isEqualTo(30.0);
        }

        @Test
        void shouldCreateOrderReference_withOnlyOrderId() {
            OrderItem domain = OrderItem.builder()
                    .id(1L).orderId(10L).productVariantId(5L)
                    .unitPrice(20.0).quantity(1)
                    .build();

            OrderItemEntity entity = mapper.toEntity(domain);

            assertThat(entity.getOrder()).isNotNull();
            assertThat(entity.getOrder().getId()).isEqualTo(10L);
            assertThat(entity.getOrder().getDeviceId()).isNull();
        }

        @Test
        void shouldCreateProductVariantReference_withOnlyVariantId() {
            OrderItem domain = OrderItem.builder()
                    .id(1L).orderId(10L).productVariantId(5L)
                    .unitPrice(20.0).quantity(1)
                    .build();

            OrderItemEntity entity = mapper.toEntity(domain);

            assertThat(entity.getProductVariant()).isNotNull();
            assertThat(entity.getProductVariant().getId()).isEqualTo(5L);
            assertThat(entity.getProductVariant().getName()).isNull();
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
        void shouldMapAllFields_andExtractForeignKeysFromReferences() {
            OrderEntity orderRef = new OrderEntity();
            orderRef.setId(10L);

            ProductVariantEntity variantRef = new ProductVariantEntity();
            variantRef.setId(5L);

            OrderItemEntity entity = new OrderItemEntity();
            entity.setId(1L);
            entity.setOrder(orderRef);
            entity.setProductVariant(variantRef);
            entity.setUnitPrice(20.0);
            entity.setQuantity(3);

            OrderItem domain = mapper.toDomain(entity);

            assertThat(domain.getId()).isEqualTo(1L);
            assertThat(domain.getOrderId()).isEqualTo(10L);
            assertThat(domain.getProductVariantId()).isEqualTo(5L);
            assertThat(domain.getUnitPrice()).isEqualTo(20.0);
            assertThat(domain.getQuantity()).isEqualTo(3);
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
            OrderEntity orderRef = new OrderEntity();
            orderRef.setId(10L);

            ProductVariantEntity variant1 = new ProductVariantEntity();
            variant1.setId(5L);
            OrderItemEntity e1 = new OrderItemEntity();
            e1.setId(1L);
            e1.setOrder(orderRef);
            e1.setProductVariant(variant1);
            e1.setUnitPrice(20.0);
            e1.setQuantity(2);

            ProductVariantEntity variant2 = new ProductVariantEntity();
            variant2.setId(6L);
            OrderItemEntity e2 = new OrderItemEntity();
            e2.setId(2L);
            e2.setOrder(orderRef);
            e2.setProductVariant(variant2);
            e2.setUnitPrice(15.0);
            e2.setQuantity(1);

            List<OrderItem> result = mapper.toDomainList(List.of(e1, e2));

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
        void shouldMapEachOrderItem() {
            OrderItem item1 = OrderItem.builder()
                    .id(1L).orderId(10L).productVariantId(5L).unitPrice(20.0).quantity(2).build();
            OrderItem item2 = OrderItem.builder()
                    .id(2L).orderId(10L).productVariantId(6L).unitPrice(15.0).quantity(1).build();

            List<OrderItemEntity> result = mapper.toEntityList(List.of(item1, item2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getOrder().getId()).isEqualTo(10L);
            assertThat(result.get(0).getProductVariant().getId()).isEqualTo(5L);
            assertThat(result.get(1).getProductVariant().getId()).isEqualTo(6L);
        }
    }
}
