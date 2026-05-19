package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.OrderEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

@NullMarked
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    Optional<OrderEntity> findById(Long id);

    Optional<OrderEntity> findByUserId(Long userId);

    boolean existsById(Long id);

    boolean existsByUserId(Long orderId);

}
