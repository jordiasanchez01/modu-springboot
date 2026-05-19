package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Order;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order saveWithoutItems(Order order);

    Optional<Order> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Order save(Order order);
}
