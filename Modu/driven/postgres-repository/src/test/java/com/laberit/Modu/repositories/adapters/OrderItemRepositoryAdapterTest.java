package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.repositories.OrderItemJpaRepository;
import com.laberit.Modu.repositories.mappers.OrderItemPersistanceMapper;
import com.laberit.Modu.repositories.models.OrderItemEntity;
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
class OrderItemRepositoryAdapterTest {

    @Mock private OrderItemJpaRepository orderItemJpaRepository;
    @Mock private OrderItemPersistanceMapper orderItemMapper;

    @InjectMocks
    private OrderItemRepositoryAdapter adapter;

    private OrderItemEntity entity;
    private OrderItem domain;

    @BeforeEach
    void setUp() {
        entity = new OrderItemEntity();
        entity.setId(1L);
        entity.setUnitPrice(20.0);
        entity.setQuantity(3);

        domain = OrderItem.builder()
                .id(1L).orderId(5L).productVariantId(10L)
                .unitPrice(20.0).quantity(3).build();
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        void shouldReturnMappedDomain_whenFound() {
            when(orderItemJpaRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(orderItemMapper.toDomain(entity)).thenReturn(domain);

            Optional<OrderItem> result = adapter.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
        }

        @Test
        void shouldReturnEmpty_whenNotFound() {
            when(orderItemJpaRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<OrderItem> result = adapter.findById(99L);

            assertThat(result).isEmpty();
            verifyNoInteractions(orderItemMapper);
        }
    }

    @Nested
    @DisplayName("findByProductVariantId()")
    class FindByProductVariantId {

        @Test
        void shouldAlwaysReturnEmpty() {
            Optional<OrderItem> result = adapter.findByProductVariantId(10L);

            assertThat(result).isEmpty();
            verifyNoInteractions(orderItemJpaRepository);
            verifyNoInteractions(orderItemMapper);
        }
    }

    @Nested
    @DisplayName("findAllByOrderId()")
    class FindAllByOrderId {

        @Test
        void shouldReturnMappedList() {
            when(orderItemJpaRepository.findAllByOrderId(5L)).thenReturn(List.of(entity));
            when(orderItemMapper.toDomainList(List.of(entity))).thenReturn(List.of(domain));

            List<OrderItem> result = adapter.findAllByOrderId(5L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }

        @Test
        void shouldReturnEmptyList_whenNoneFound() {
            when(orderItemJpaRepository.findAllByOrderId(99L)).thenReturn(List.of());
            when(orderItemMapper.toDomainList(List.of())).thenReturn(List.of());

            assertThat(adapter.findAllByOrderId(99L)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAllByProductVariantId()")
    class FindAllByProductVariantId {

        @Test
        void shouldAlwaysReturnEmptyList() {
            List<OrderItem> result = adapter.findAllByProductVariantId(10L);

            assertThat(result).isEmpty();
            verifyNoInteractions(orderItemJpaRepository);
            verifyNoInteractions(orderItemMapper);
        }
    }

    @Nested
    @DisplayName("existsByProductVariantId()")
    class ExistsByProductVariantId {

        @Test
        void shouldAlwaysReturnFalse() {
            assertThat(adapter.existsByProductVariantId(10L)).isFalse();
            verifyNoInteractions(orderItemJpaRepository);
        }
    }

    @Nested
    @DisplayName("save()")
    class Save {

        @Test
        void shouldConvertToEntity_persist_andConvertBack() {
            when(orderItemMapper.toEntity(domain)).thenReturn(entity);
            when(orderItemJpaRepository.save(entity)).thenReturn(entity);
            when(orderItemMapper.toDomain(entity)).thenReturn(domain);

            OrderItem result = adapter.save(domain);

            assertThat(result.getId()).isEqualTo(1L);
            verify(orderItemJpaRepository).save(entity);
        }
    }

    @Nested
    @DisplayName("saveAll()")
    class SaveAll {

        @Test
        void shouldConvertAll_persistAll_andConvertBack() {
            List<OrderItem> domainItems = List.of(domain);
            List<OrderItemEntity> entityItems = List.of(entity);

            when(orderItemMapper.toEntityList(domainItems)).thenReturn(entityItems);
            when(orderItemJpaRepository.saveAll(entityItems)).thenReturn(entityItems);
            when(orderItemMapper.toDomainList(entityItems)).thenReturn(domainItems);

            List<OrderItem> result = adapter.saveAll(domainItems);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo(1L);
        }
    }
}
