package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.ports.driven.OrderItemRepositoryPort;
import com.laberit.Modu.repositories.OrderItemJpaRepository;
import com.laberit.Modu.repositories.mappers.OrderItemPersistanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderItemRepositoryAdapter implements OrderItemRepositoryPort {
    private final OrderItemJpaRepository orderItemJpaRepository;
    private final OrderItemPersistanceMapper orderItemMapper;

    @Override
    public Optional<OrderItem> findById(Long id) {
        return orderItemJpaRepository.findById(id).map(orderItemMapper::toDomain);
    }

    @Override
    public Optional<OrderItem> findByProductVariantId(Long productVariantId) {
        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAllByOrderId(Long orderId) {
        return orderItemMapper.toDomainList(orderItemJpaRepository.findAllByOrderId(orderId));
    }

    @Override
    public List<OrderItem> findAllByProductVariantId(Long productVariantId) {
        return List.of();
    }

    @Override
    public boolean existsByProductVariantId(Long productVariantId) {
        return false;
    }

    @Override
    public OrderItem save(OrderItem orderItem) {
        return orderItemMapper.toDomain(orderItemJpaRepository.save(orderItemMapper.toEntity(orderItem)));
    }
}
