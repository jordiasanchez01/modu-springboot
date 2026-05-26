package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.*;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driven.ProductRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceUseCase implements CartItemServicePort {
    private final ProductVariantServicePort productVariantServicePort;
    private final CartItemRepositoryPort cartItemRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;
    private final ProductVariantRepositoryPort productVariantRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    public CartItem updateCartItemQuantity(String deviceId, Long cartItemId, UpdateCartItemQuantityCommand command) {
        int requestedQuantity = command.quantity();
        Cart cart = cartRepositoryPort.findByDeviceId(deviceId).orElseThrow(
                CartNotFoundException::new);
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
    public void deleteCartItemById(String deviceId, Long itemId) {
        Cart cart = cartRepositoryPort.findByDeviceId(deviceId).orElseThrow(
                CartNotFoundException::new);
        CartItem item = cartItemRepositoryPort.findByIdAndCartId(itemId, cart.getId())
                .orElseThrow(() -> new CartItemNotFoundException(itemId));
        cartItemRepositoryPort.deleteById(itemId);
    }

    @Override
    public void deleteAllCartItems(String deviceId) {
        Cart cart = cartRepositoryPort.findByDeviceId(deviceId).orElseThrow(CartNotFoundException::new);
        cartItemRepositoryPort.deleteAllByCartId(cart.getId());
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

    @Override
    @Transactional
    public Cart addCartItemToCart(AddCartItemCommand command) {
        ProductVariant variant = getProductVariant(command.productVariantId());
        Product product = getProduct(variant.getProductId());

        Cart cart = cartRepositoryPort.findByDeviceId(command.deviceId())
                .orElseThrow(CartNotFoundException::new);

        Optional<CartItem> existingItem = cartItemRepositoryPort
                .findByCartIdAndProductVariantId(cart.getId(), command.productVariantId());

        int totalQuantity = existingItem.map(item -> item.getQuantity() + command.quantity())
                .orElse(command.quantity());

        productVariantServicePort.assertIsValidToPurchase(variant, totalQuantity);

        CartItem item = buildCartItem(existingItem, cart, command, product, variant);
        cartItemRepositoryPort.save(item);

        cart.setCartItems(cartItemRepositoryPort.findAllByCartId(cart.getId()));
        Cart updatedCart = cartRepositoryPort.findByDeviceId(command.deviceId()).orElseThrow(CartNotFoundException::new);
        Set<Long> variantIds = updatedCart.getCartItems().stream()
                .map(CartItem::getProductVariantId)
                .collect(Collectors.toSet());
        Set<ProductVariant> variants = productVariantRepositoryPort.findAllByIdInSet(variantIds);
        updatedCart.setCartItems(setProductIdsInItems(updatedCart.getCartItems(), variants));
        return updatedCart;
    }

    private CartItem buildCartItem(
            Optional<CartItem> existing,
            Cart cart,
            AddCartItemCommand command,
            Product product,
            ProductVariant variant
    ) {
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + command.quantity());
            item.setCurrentStock(variant.getStock());
            return item;
        }

        return CartItem.builder()
                .cartId(cart.getId())
                .productVariantId(command.productVariantId())
                .unitPrice(product.getPrice())
                .quantity(command.quantity())
                .currentStock(variant.getStock())
                .build();
    }

    private Product getProduct(Long productId) {
        return productRepositoryPort.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId.toString()));
    }

    private ProductVariant getProductVariant(Long id) {
        return productVariantRepositoryPort.findById(id)
                .orElseThrow(()-> new ProductVariantNotFoundException(
                        id.toString()
                ));
    }

    private List<CartItem> setProductIdsInItems(List<CartItem> cartItems, Set<ProductVariant> variants) {
        Map<Long, ProductVariant> variantMap = variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, v -> v));

        cartItems.forEach(item -> item.setProductId(
                variantMap.get(item.getProductVariantId()).getProductId()));
        return cartItems;
    }
}
