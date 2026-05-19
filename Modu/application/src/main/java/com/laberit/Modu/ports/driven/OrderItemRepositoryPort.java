package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepositoryPort {

    Optional<OrderItem> findById(Long id);

    Optional<OrderItem> findByProductVariantId(Long productVariantId);

    List<OrderItem> findAllByOrderId(Long orderId);

    List<OrderItem> findAllByProductVariantId(Long productVariantId);

    boolean existsByProductVariantId(Long productVariantId);

    OrderItem save(OrderItem orderItem);

}
