package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.repositories.OrderItemJpaRepository;
import com.laberit.Modu.repositories.OrderJpaRepository;
import com.laberit.Modu.repositories.mappers.OrderItemPersistanceMapper;
import com.laberit.Modu.repositories.mappers.OrderPersistanceMapper;
import com.laberit.Modu.repositories.models.OrderEntity;
import com.laberit.Modu.repositories.models.OrderItemEntity;
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
class OrderRepositoryAdapterTest {

    @Mock private OrderJpaRepository orderJpaRepository;
    @Mock private OrderItemJpaRepository orderItemJpaRepository;
    @Mock private OrderPersistanceMapper orderMapper;
    @Mock private OrderItemPersistanceMapper orderItemMapper;
    @Mock private EntityManager entityManager;

    @InjectMocks
    private OrderRepositoryAdapter adapter;

    private OrderEntity orderEntity;
    private OrderItemEntity orderItemEntity;
    private Order domainOrder;
    private OrderItem domainOrderItem;

    @BeforeEach
    void setUp() {
        orderItemEntity = new OrderItemEntity();
        orderItemEntity.setId(10L);
        orderItemEntity.setUnitPrice(25.0);
        orderItemEntity.setQuantity(2);

        orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setDeviceId("device-xyz");
        orderEntity.setShippingCosts(5.0);
        orderEntity.setOrderItems(List.of(orderItemEntity));

        domainOrderItem = OrderItem.builder()
                .id(10L).orderId(1L).productVariantId(7L).unitPrice(25.0).quantity(2).build();

        domainOrder = Order.builder()
                .id(1L).deviceId("device-xyz").shippingCosts(5.0)
                .orderItems(List.of(domainOrderItem)).build();
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        void shouldSaveOrderAndItems_thenRefreshAndReturnDomain() {
            when(orderMapper.toEntity(domainOrder)).thenReturn(orderEntity);
            when(orderJpaRepository.save(orderEntity)).thenReturn(orderEntity);
            when(orderItemMapper.toEntityList(domainOrder.getOrderItems())).thenReturn(List.of(orderItemEntity));
            when(orderMapper.toDomain(orderEntity)).thenReturn(domainOrder);
            when(orderItemMapper.toDomainList(orderEntity.getOrderItems())).thenReturn(List.of(domainOrderItem));

            Order result = adapter.save(domainOrder);

            assertThat(result.getDeviceId()).isEqualTo("device-xyz");
            assertThat(result.getOrderItems()).hasSize(1);
            verify(orderJpaRepository).save(orderEntity);
            verify(orderItemJpaRepository).saveAll(anyList());
            verify(entityManager).flush();
            verify(entityManager).refresh(orderEntity);
        }

        @Test
        void shouldAssignOrderEntityToEachItem_beforeSavingItems() {
            OrderItemEntity itemWithoutOrder = new OrderItemEntity();
            orderEntity.setOrderItems(List.of(itemWithoutOrder));

            when(orderMapper.toEntity(domainOrder)).thenReturn(orderEntity);
            when(orderJpaRepository.save(orderEntity)).thenReturn(orderEntity);
            when(orderItemMapper.toEntityList(domainOrder.getOrderItems())).thenReturn(List.of(itemWithoutOrder));
            when(orderMapper.toDomain(orderEntity)).thenReturn(domainOrder);
            when(orderItemMapper.toDomainList(orderEntity.getOrderItems())).thenReturn(List.of(domainOrderItem));

            adapter.save(domainOrder);

            assertThat(itemWithoutOrder.getOrder()).isSameAs(orderEntity);
        }
    }

    @Nested
    @DisplayName("saveWithoutItems()")
    class SaveWithoutItems {

        @Test
        void shouldSaveOrderOnly_withoutTouchingItemRepository() {
            Order orderWithoutItems = Order.builder().id(1L).deviceId("device-xyz").build();
            when(orderMapper.toEntity(orderWithoutItems)).thenReturn(orderEntity);
            when(orderJpaRepository.save(orderEntity)).thenReturn(orderEntity);
            when(orderMapper.toDomain(orderEntity)).thenReturn(orderWithoutItems);

            Order result = adapter.saveWithoutItems(orderWithoutItems);

            assertThat(result.getDeviceId()).isEqualTo("device-xyz");
            verify(orderJpaRepository).save(orderEntity);
            verifyNoInteractions(orderItemJpaRepository);
            verifyNoInteractions(entityManager);
        }
    }

    @Nested
    @DisplayName("findByDeviceId()")
    class FindByDeviceId {

        @Test
        void shouldReturnMappedDomain_andPopulateItems_whenFound() {
            when(orderJpaRepository.findByDeviceId("device-xyz")).thenReturn(Optional.of(orderEntity));
            when(orderMapper.toDomain(orderEntity)).thenReturn(domainOrder);
            when(orderItemMapper.toDomainList(orderEntity.getOrderItems())).thenReturn(List.of(domainOrderItem));

            Optional<Order> result = adapter.findByDeviceId("device-xyz");

            assertThat(result).isPresent();
            assertThat(result.get().getDeviceId()).isEqualTo("device-xyz");
            assertThat(result.get().getOrderItems()).hasSize(1);
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(orderJpaRepository.findByDeviceId("unknown")).thenReturn(Optional.empty());

            Optional<Order> result = adapter.findByDeviceId("unknown");

            assertThat(result).isEmpty();
            verifyNoInteractions(orderMapper);
        }
    }

    @Nested
    @DisplayName("findByOrderId()")
    class FindByOrderId {

        @Test
        void shouldReturnMappedDomain_andPopulateItems_whenFound() {
            when(orderJpaRepository.findById(1L)).thenReturn(Optional.of(orderEntity));
            when(orderMapper.toDomain(orderEntity)).thenReturn(domainOrder);
            when(orderItemMapper.toDomainList(orderEntity.getOrderItems())).thenReturn(List.of(domainOrderItem));

            Optional<Order> result = adapter.findByOrderId(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            assertThat(result.get().getOrderItems()).hasSize(1);
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(orderJpaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Order> result = adapter.findByOrderId(99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(orderMapper);
        }
    }

    @Nested
    @DisplayName("existsByDeviceId()")
    class ExistsByDeviceId {

        @Test
        void shouldReturnTrue_whenExists() {
            when(orderJpaRepository.existsByDeviceId("device-xyz")).thenReturn(true);
            assertThat(adapter.existsByDeviceId("device-xyz")).isTrue();
        }

        @Test
        void shouldReturnFalse_whenNotExists() {
            when(orderJpaRepository.existsByDeviceId("unknown")).thenReturn(false);
            assertThat(adapter.existsByDeviceId("unknown")).isFalse();
        }
    }
}
