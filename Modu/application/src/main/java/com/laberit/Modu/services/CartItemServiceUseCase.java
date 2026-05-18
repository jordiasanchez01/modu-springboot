package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.*;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.UpdateCartItemCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceUseCase implements CartItemServicePort {
    private final CartItemRepositoryPort cartItemRepositoryPort;
    private final ProductVariantServicePort productVariantServicePort;
    private final CartRepositoryPort cartRepositoryPort;

    @Override
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemCommand command) {
        int requestedQuantity = command.quantity();
        Cart cart = cartRepositoryPort.findByUserId(userId).orElseThrow(
                () -> new CartNotFoundException(userId));
        CartItem item = cartItemRepositoryPort.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        productVariantServicePort.assertIsValidToPurchase(item.getProductVariantId(), requestedQuantity);
        item.setQuantity(requestedQuantity);
        return cartItemRepositoryPort.save(item);
    }

}
