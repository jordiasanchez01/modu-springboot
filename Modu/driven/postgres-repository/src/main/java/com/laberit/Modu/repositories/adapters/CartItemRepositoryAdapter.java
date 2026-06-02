package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.repositories.CartItemJpaRepository;
import com.laberit.Modu.repositories.mappers.CartItemPersistanceMapper;
import com.laberit.Modu.repositories.models.CartItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartItemRepositoryAdapter implements CartItemRepositoryPort {
    private final CartItemJpaRepository cartItemJpaRepository;
    private final CartItemPersistanceMapper cartItemMapper;

    @Override
    public Optional<CartItem> findById(Long id) {
        return cartItemJpaRepository.findById(id).map(cartItemMapper::toDomain);
    }

    @Override
    public Optional<CartItem> findByProductVariantId(Long productVariantId) {
        return Optional.empty();
    }

    @Override
    public Optional<CartItem> findByIdAndCartId(Long id, Long cartId) {
        return cartItemJpaRepository.findByIdAndCartId(id, cartId).map(cartItemMapper::toDomain);
    }

    @Override
    public Optional<CartItem> findByCartIdAndProductVariantId(Long cartId, Long productVariantId) {
        return cartItemJpaRepository.findByCartIdAndProductVariantId(cartId,productVariantId)
                .map(cartItemMapper::toDomain);
    }

    @Override
    public List<CartItem> findAllByCartId(Long cartId) {
        return cartItemMapper.toDomainList(cartItemJpaRepository.findAllByCartId(cartId));
    }

    @Override
    public void deleteById(Long id) {
        cartItemJpaRepository.deleteById(id);
        cartItemJpaRepository.flush();
    }

    @Override
    public void deleteAllByCartId(Long cartId) {
        cartItemJpaRepository.deleteAllByCartId(cartId);
        cartItemJpaRepository.flush();
    }

    @Override
    public void deleteAllByIdIn(List<Long> ids) {
        cartItemJpaRepository.deleteAllByIdIn(ids);
        cartItemJpaRepository.flush();
    }

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemMapper.toDomain(cartItemJpaRepository.save(cartItemMapper.toEntity(cartItem)));
    }

    @Override
    public List<CartItem> saveAll(List<CartItem> cartItems) {
        List<CartItemEntity> items = cartItemJpaRepository.saveAll(cartItemMapper.toEntityList(cartItems));
        cartItemJpaRepository.flush();
        return cartItemMapper.toDomainList(items);
    }
}
