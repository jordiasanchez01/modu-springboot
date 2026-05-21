package com.laberit.Modu.repositories;

import com.laberit.Modu.repositories.models.CartEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@NullMarked
public interface CartJpaRepository extends JpaRepository<CartEntity, Long>, JpaSpecificationExecutor<CartEntity> {

    Optional<CartEntity> findById(Long id);

    Optional<CartEntity> findByDeviceId(String deviceId);

    boolean existsById(Long id);

    boolean existsByDeviceId(String deviceId);

}
