package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Order;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order saveWithoutItems(Order order);

    Optional<Order> findByDeviceId(String deviceId);

    Optional<Order> findByOrderId(Long orderId);

    boolean existsByDeviceId(String deviceId);

    Order save(Order order);
}
