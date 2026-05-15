package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.CartItemEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long>, JpaSpecificationExecutor<CartItemEntity> {

    List<CartItemEntity> findAllByCartId(Long id);

    Optional<CartItemEntity> findById(Long id);

    Optional<CartItemEntity> findByProductVariantId(Long productVariantId);

    Optional<CartItemEntity> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    boolean existsById(Long id);

    boolean existsByProductVariantId(Long productVariantId);

}
