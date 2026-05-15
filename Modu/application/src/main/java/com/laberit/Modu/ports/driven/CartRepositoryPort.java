package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Cart;
import java.util.Optional;

public interface CartRepositoryPort {

    Optional<Cart> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    Cart save(Cart cart);

    void deleteByUserId(Long userId);
}
