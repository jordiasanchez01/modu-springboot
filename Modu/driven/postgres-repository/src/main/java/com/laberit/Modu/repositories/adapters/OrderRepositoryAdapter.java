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
        OrderEntity savedOrder = orderJpaRepository.save(orderMapper.toEntity(order));

        List<OrderItemEntity> items = orderItemMapper.toEntityList(order.getOrderItems());
        items.forEach(item -> item.setOrder(savedOrder));
        orderItemJpaRepository.saveAll(items);

        entityManager.flush();
        entityManager.refresh(savedOrder);

        Order result = orderMapper.toDomain(savedOrder);
        result.setOrderItems(orderItemMapper.toDomainList(savedOrder.getOrderItems()));
        return result;
    }

    @Override
    public Order saveWithoutItems(Order order) {
        OrderEntity savedOrder = orderJpaRepository.save(orderMapper.toEntity(order));
        return orderMapper.toDomain(savedOrder);
    }

    @Override
    public Optional<Order> findByDeviceId(String deviceId) {
        return orderJpaRepository.findByDeviceId(deviceId).map(entity -> {
            Order order = orderMapper.toDomain(entity);
            order.setOrderItems(orderItemMapper.toDomainList(entity.getOrderItems()));
            return order;
        });
    }

    @Override
    public Optional<Order> findByOrderId(Long orderId) {
        return orderJpaRepository.findById(orderId).map(entity -> {
            Order order = orderMapper.toDomain(entity);
            order.setOrderItems(orderItemMapper.toDomainList(entity.getOrderItems()));
            return order;
        });
    }

    @Override
    public boolean existsByDeviceId(String deviceId) {
        return orderJpaRepository.existsByDeviceId(deviceId);
    }
}
