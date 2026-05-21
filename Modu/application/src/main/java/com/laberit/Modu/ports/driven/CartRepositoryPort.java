package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.Cart;
import java.util.Optional;

public interface CartRepositoryPort {

    Optional<Cart> findByDeviceId(String deviceId);

    boolean existsByDeviceId(String deviceId);

    Cart save(Cart cart);

    Cart saveAndFlush(Cart cart);
}
