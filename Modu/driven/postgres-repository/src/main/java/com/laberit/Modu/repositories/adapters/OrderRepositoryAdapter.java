package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.ports.driven.OrderRepositoryPort;
import com.laberit.Modu.repositories.OrderItemJpaRepository;
import com.laberit.Modu.repositories.OrderJpaRepository;
import com.laberit.Modu.repositories.mappers.OrderItemPersistanceMapper;
import com.laberit.Modu.repositories.mappers.OrderPersistanceMapper;
import com.laberit.Modu.repositories.models.OrderEntity;
import com.laberit.Modu.repositories.models.OrderItemEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;
    private final OrderPersistanceMapper orderMapper;
    private final OrderItemPersistanceMapper orderItemMapper;
    private final EntityManager entityManager;


    @Override
    public Order save(Order order) {
        // Save order first to get the confirmed ID from the database
        OrderEntity savedOrder = orderJpaRepository.save(orderMapper.toEntity(order));

        // Now save order items with the confirmed order ID
        List<OrderItemEntity> items = orderItemMapper.toEntityList(order.getOrderItems());
        items.forEach(item -> item.setOrder(savedOrder));
        orderItemJpaRepository.saveAll(items);

        // Force Hibernate to discard cached entity and re-read from database
        entityManager.flush();
        entityManager.refresh(savedOrder);

        Order result = orderMapper.toDomain(savedOrder);
        result.setOrderItems(orderItemMapper.toDomainList(savedOrder.getOrderItems()));
        return result;
    }

    @Override
    public Optional<Order> findByUserId(Long userId) {
        return orderJpaRepository.findByUserId(userId).map(entity -> {
            Order order = orderMapper.toDomain(entity);
            order.setOrderItems(orderItemMapper.toDomainList(entity.getOrderItems()));
            return order;
        });
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return orderJpaRepository.existsByUserId(userId);
    }
}
