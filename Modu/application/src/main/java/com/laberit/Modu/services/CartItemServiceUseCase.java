package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.*;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceUseCase implements CartItemServicePort {
    private final CartItemRepositoryPort cartItemRepositoryPort;
    private final ProductVariantServicePort productVariantServicePort;
    private final CartRepositoryPort cartRepositoryPort;
    private final ProductVariantRepositoryPort productVariantRepositoryPort;

    @Override
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemQuantityCommand command) {
        int requestedQuantity = command.quantity();
        Cart cart = cartRepositoryPort.findByUserId(userId).orElseThrow(
                () -> new CartNotFoundException(userId));
        CartItem item = cartItemRepositoryPort.findByIdAndCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(cartItemId));
        ProductVariant productVariant = productVariantRepositoryPort.findById(item.getProductVariantId())
                .orElseThrow(() -> new ProductVariantNotFoundException(item.getProductVariantId().toString()));
        productVariantServicePort.assertIsValidToPurchase(productVariant, requestedQuantity);
        item.setQuantity(requestedQuantity);
        cartItemRepositoryPort.save(item);
        return cartItemRepositoryPort.save(item);
    }

    @Override
    public void deleteCartItemById(Long userId, Long itemId) {
        Cart cart = cartRepositoryPort.findByUserId(userId).orElseThrow(
                () -> new CartNotFoundException(userId));
        CartItem item = cartItemRepositoryPort.findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));
        cartItemRepositoryPort.deleteById(itemId);
    }

    @Override
    public void deleteAllCartItems(Long userId) {
        Cart cart = cartRepositoryPort.findByUserId(userId).orElseThrow(() -> new CartNotFoundException(userId));
        if (cart!=null) {
            cartItemRepositoryPort.deleteAllByCartId(cart.getId());
        }
    }

    @Override
    public Set<InsufficientStockResult> checkStockOfCartItems(Set<CartItem> cartItems) {
        Set<Long> variantIds = cartItems.stream()
                .map(CartItem::getProductVariantId)
                .collect(Collectors.toSet());

        Map<Long, Integer> quantityMap = cartItems.stream()
                .collect(Collectors.toMap(
                        CartItem::getProductVariantId,
                        CartItem::getQuantity
                ));

        List<ProductVariant> variants = productVariantServicePort.findAllByIdInSet(variantIds)
                .stream()
                .toList();

        Set<InsufficientStockResult> insufficientStockResults = new HashSet<>();

        variants.forEach(variant -> {
                    int stockResult = (variant.getStock() - quantityMap.get(variant.getId()));
                    if (stockResult < 0) {
                        InsufficientStockResult result = new InsufficientStockResult(
                                variant.getId(),
                                quantityMap.get(variant.getId()),
                                variant.getStock()
                        );
                        insufficientStockResults.add(result);
                    }
                }
        );

        return insufficientStockResults;
    }
}
