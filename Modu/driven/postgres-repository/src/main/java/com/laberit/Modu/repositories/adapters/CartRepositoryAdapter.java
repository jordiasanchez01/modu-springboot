package com.laberit.Modu.repositories.adapters;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.repositories.CartItemJpaRepository;
import com.laberit.Modu.repositories.CartJpaRepository;
import com.laberit.Modu.repositories.mappers.CartItemPersistanceMapper;
import com.laberit.Modu.repositories.mappers.CartPersistanceMapper;
import com.laberit.Modu.repositories.models.CartEntity;
import com.laberit.Modu.repositories.models.CartItemEntity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartRepositoryAdapter implements CartRepositoryPort {
    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;
    private final CartPersistanceMapper cartMapper;
    private final CartItemPersistanceMapper cartItemMapper;
    private final EntityManager entityManager;

    @Override
    public Cart save(Cart cart) {

        CartEntity savedCart = cartJpaRepository.save(cartMapper.toEntity(cart));
        List<CartItemEntity> items = cartItemMapper.toEntityList(cart.getCartItems());
        items.forEach(item -> item.setCartId(savedCart.getId()));

        cartItemJpaRepository.saveAll(items);
        entityManager.flush();
        entityManager.refresh(savedCart);

        Cart result = cartMapper.toDomain(savedCart);
        result.setCartItems(cartItemMapper.toDomainList(savedCart.getCartItems()));
        return result;
    }

    @Override
    public void deleteByDeviceId(String deviceId) {
        cartJpaRepository.deleteByDeviceId(deviceId);
    }

    @Override
    public Optional<Cart> findByDeviceId(String deviceId) {
        return cartJpaRepository.findByDeviceId(deviceId).map(entity -> {
            entityManager.refresh(entity);
            Cart cart = cartMapper.toDomain(entity);
            cart.setCartItems(cartItemMapper.toDomainList(entity.getCartItems()));
            return cart;
        });
    }

    @Override
    public boolean existsByDeviceId(String deviceId) {
        return cartJpaRepository.existsByDeviceId(deviceId);
    }
}
