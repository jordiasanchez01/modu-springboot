package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.repositories.CartJpaRepository;
import com.laberit.Modu.repositories.mappers.CartPersistanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartRepositoryAdapter implements CartRepositoryPort {
    private final CartJpaRepository cartJpaRepository;
    private final CartPersistanceMapper cartMapper;


    @Override
    public Optional<Cart> findByUserId(Long userId) {
        return cartJpaRepository.findByUserId(userId).map(cartMapper::toDomain);
    }

    @Override
    public boolean existsByUserId(Long userId) {
        return cartJpaRepository.existsByUserId(userId);
    }

    @Override
    public Cart save(Cart cart) {
        return cartMapper.toDomain(
                cartJpaRepository.save(
                        cartMapper.toEntity(cart)
                )
        );
    }
}
