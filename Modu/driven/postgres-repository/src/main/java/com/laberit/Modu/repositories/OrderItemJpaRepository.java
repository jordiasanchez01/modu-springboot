package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.OrderItemEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long>, JpaSpecificationExecutor<OrderItemEntity> {

    List<OrderItemEntity> findAllByOrderId(Long id);

    Optional<OrderItemEntity> findById(Long id);

    Optional<OrderItemEntity> findByProductVariantId(Long productVariantId);

    boolean existsById(Long id);

    boolean existsByProductVariantId(Long productVariantId);

}
