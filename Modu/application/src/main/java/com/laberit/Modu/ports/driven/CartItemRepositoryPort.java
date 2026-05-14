package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepositoryPort {
    CartItem save(CartItem cartItem);
    List<CartItem> saveAll(List<CartItem> cartItems);

    Optional<CartItem> findById(Long id);

    Optional<CartItem> findByProductVariantId(Long productVariantId);
    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);

    List<CartItem> findAllByCartId(Long cartId);

    List<CartItem> findAllByProductVariantId(Long productVariantId);

    boolean existsByProductVariantId(Long productVariantId);
}
