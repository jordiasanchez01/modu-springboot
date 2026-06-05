package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.repositories.models.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistanceMapperTest {

    private OrderPersistanceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new OrderPersistanceMapperImpl();
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldReturnNull_whenOrderIsNull() {
            assertThat(mapper.toEntity(null)).isNull();
        }

        @Test
        void shouldMapHeaderFields() {
            Order order = Order.builder()
                    .id(1L).deviceId("device-abc")
                    .specialInstructions("Leave at door")
                    .shippingCosts(5.0)
                    .build();

            OrderEntity entity = mapper.toEntity(order);

            assertThat(entity.getId()).isEqualTo(1L);
            assertThat(entity.getDeviceId()).isEqualTo("device-abc");
            assertThat(entity.getSpecialInstructions()).isEqualTo("Leave at door");
            assertThat(entity.getShippingCosts()).isEqualTo(5.0);
        }

        @Test
        void shouldComputeAndStoreSubtotalAndTotalPrice() {
            // subtotalPrice and totalPrice are computed from the domain model
            // and persisted so they can be read directly from the DB.
            OrderItem item = OrderItem.builder()
                    .unitPrice(10.0).quantity(3).build(); // totalPrice = 30.0
            Order order = Order.builder()
                    .id(1L).deviceId("device-abc")
                    .shippingCosts(5.0)
                    .orderItems(List.of(item))
                    .build();

            OrderEntity entity = mapper.toEntity(order);

            assertThat(entity.getSubtotalPrice()).isEqualTo(30.0);
            assertThat(entity.getTotalPrice()).isEqualTo(35.0); // subtotal + shipping
        }

        @Test
        void shouldStoreZeroSubtotalAndTotalEqualToShipping_whenNoItems() {
            Order order = Order.builder()
                    .id(1L).deviceId("device-abc")
                    .shippingCosts(10.0)
                    .build();

            OrderEntity entity = mapper.toEntity(order);

            assertThat(entity.getSubtotalPrice()).isEqualTo(0.0);
            assertThat(entity.getTotalPrice()).isEqualTo(10.0);
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
        void shouldMapHeaderFields_andPreserveTimestamp() {
            Instant now = Instant.now();
            OrderEntity entity = new OrderEntity();
            entity.setId(1L);
            entity.setDeviceId("device-abc");
            entity.setSpecialInstructions("Leave at door");
            entity.setShippingCosts(5.0);
            entity.setCreatedAt(now);

            Order order = mapper.toDomain(entity);

            assertThat(order.getId()).isEqualTo(1L);
            assertThat(order.getDeviceId()).isEqualTo("device-abc");
            assertThat(order.getSpecialInstructions()).isEqualTo("Leave at door");
            assertThat(order.getShippingCosts()).isEqualTo(5.0);
            assertThat(order.getCreatedAt()).isEqualTo(now);
        }

        @Test
        void shouldNotMapOrderItems() {
            // orderItems are populated by the adapter after loading the entity;
            // toDomain must not forward the entity's item collection.
            OrderEntity entity = new OrderEntity();
            entity.setId(1L);
            entity.setDeviceId("device-abc");
            entity.setShippingCosts(0.0);

            Order order = mapper.toDomain(entity);

            assertThat(order.getOrderItems()).isEmpty();
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
            OrderEntity e1 = new OrderEntity();
            e1.setId(1L);
            e1.setDeviceId("device-001");
            e1.setShippingCosts(0.0);
            OrderEntity e2 = new OrderEntity();
            e2.setId(2L);
            e2.setDeviceId("device-002");
            e2.setShippingCosts(5.0);

            List<Order> result = mapper.toDomainList(List.of(e1, e2));

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1L);
            assertThat(result.get(1).getDeviceId()).isEqualTo("device-002");
        }
    }
}
