package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.*;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartItemCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemUseCase implements CartItemServicePort {
    private final CartItemRepositoryPort cartItemRepositoryPort;
    private final ProductVariantRepositoryPort productVariantRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;
    @Override
    public CartItem findCartItemById(Long CartItemId) {
        return null;
    }

    @Override
    public CartItem findCartItemByName(String name) {
        return null;
    }

    @Override
    public CartItem addCartItem(AddCartItemCommand command) {
        return null;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, UpdateCartItemCommand command) {
        int requestedQuantity = command.quantity();
        Cart cart = cartRepositoryPort.findByUserId(userId).orElseThrow(
                () -> new CartNotFoundException(userId));
        CartItem item = cartItemRepositoryPort.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        ProductVariant productVariant = productVariantRepositoryPort.findById(item.getProductVariantId())
                        .orElseThrow(() -> new ProductVariantNotAvailableException(item.getProductVariantId().toString()));
        if (!productVariant.getActive()) throw new ProductVariantDataIntegrityException(item.getProductVariantId());
        if (productVariant.getStock() >= requestedQuantity) item.setQuantity(requestedQuantity);
        else throw new NotEnoughStockException(productVariant.getProductId());
        return cartItemRepositoryPort.save(item);
    }

    @Override
    public void deleteCartItem(Long CartItemId) {

    }
}
