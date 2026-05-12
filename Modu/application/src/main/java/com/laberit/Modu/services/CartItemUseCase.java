package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartItemNotFoundException;
import com.laberit.Modu.domain.exceptions.NotEnoughStockException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
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
    public CartItem updateCartItem(UpdateCartItemCommand command) {
        Long cartItemId = command.cartItemId();
        Long cartId = command.cartId();
        int requestedQuantity = command.quantity();
        CartItem item = cartItemRepositoryPort.findByIdAndCartId(command.cartItemId(), command.cartId())
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        ProductVariant productVariant = productVariantRepositoryPort.findById(item.getProductVariantId())
                        .orElseThrow(() -> new ProductVariantNotFoundException(item.getProductVariantId()));
        if (productVariant.getStock() >= requestedQuantity) item.setQuantity(requestedQuantity);
        else throw new NotEnoughStockException(productVariant.getProductId());
        return cartItemRepositoryPort.save(item);
    }

    @Override
    public void deleteCartItem(Long CartItemId) {

    }
}
