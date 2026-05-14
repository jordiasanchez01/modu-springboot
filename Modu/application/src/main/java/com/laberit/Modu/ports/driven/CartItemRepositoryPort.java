package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepositoryPort {

    Optional<CartItem> findById(Long id);

    Optional<CartItem> findByProductVariantId(Long productVariantId);

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    List<CartItem> findAllByCartId(Long cartId);

    List<CartItem> findAllByProductVariantId(Long productVariantId);

    boolean existsByProductVariantId(Long productVariantId);

    CartItem save(CartItem cartItem);

}
