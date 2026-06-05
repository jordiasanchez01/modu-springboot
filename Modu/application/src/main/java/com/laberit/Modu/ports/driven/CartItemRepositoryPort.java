package com.laberit.Modu.ports.driven;

import com.laberit.Modu.domain.model.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepositoryPort {

    Optional<CartItem> findById(Long id);

    Optional<CartItem> findByProductVariantId(Long productVariantId);

    Optional<CartItem> findByIdAndCartId(Long id, Long cartId);

    Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId);

    List<CartItem> findAllByCartId(Long cartId);

    CartItem save(CartItem cartItem);

    List<CartItem> saveAll(List<CartItem> cartItems);

    void deleteById(Long id);

    void deleteAllByCartId(Long cartId);

    void deleteAllByIdIn(List<Long> ids);
}
