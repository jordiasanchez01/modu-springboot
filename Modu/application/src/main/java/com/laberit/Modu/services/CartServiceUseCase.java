package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.domain.model.response.*;
import com.laberit.Modu.ports.driven.*;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceUseCase implements CartServicePort {
    private final CartRepositoryPort cartRepositoryPort;
    private final CartItemRepositoryPort cartItemRepositoryPort;
    private final ProductVariantRepositoryPort productVariantRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final ProductVariantServicePort productVariantServicePort;

    @Transactional
    public Cart initializeCart(String deviceId) {
        if (cartRepositoryPort.existsByDeviceId(deviceId)) {
            cartRepositoryPort.deleteByDeviceId(deviceId);
        }
        return cartRepositoryPort.save(
                Cart.builder().deviceId(deviceId).cartItems(new ArrayList<>()).build());
    }

    @Transactional
    public CartWithPriceAndStockCheck getCartWithPriceAndStockCheck(String deviceId) {
        Cart cart = cartRepositoryPort.findByDeviceId(deviceId)
                .orElseThrow(CartNotFoundException::new);
        List<CartItem> cartItems = cartItemRepositoryPort.findAllByCartId(cart.getId());

        Set<Long> variantIds = cartItems.stream()
                .map(CartItem::getProductVariantId)
                .collect(Collectors.toSet());
        Set<ProductVariant> variants = productVariantRepositoryPort.findAllByIdInSet(variantIds);

        updateCurrentStock(cartItems, variants);

        setProductIdsInItems(cartItems, variants);

        List<ProductPriceChange> priceChanges = detectPriceChanges(cartItems, variants);

        List<InsufficientStockResult> insufficientStock = detectInsufficientStock(cartItems);

        if (!priceChanges.isEmpty()) {
            applyPriceChanges(cartItems, priceChanges);
            cartItemRepositoryPort.saveAll(cartItems);
        }
        if (!insufficientStock.isEmpty()) {
            applyQuantityChanges(cartItems, insufficientStock);
            cartItemRepositoryPort.saveAll(cartItems);
        }

        cart.setCartItems(cartItems);
        return new CartWithPriceAndStockCheck(cart, priceChanges, insufficientStock);
    }

    @Override
    public LocalDateTime getCartUpdatedAt(String deviceId){
        Cart cart = cartRepositoryPort.findByDeviceId(deviceId)
                .orElseThrow(CartNotFoundException::new);
        return cart.getUpdatedAt();
    }

    @Override
    public Cart findCartByDeviceId(String deviceId) {

        Cart cart = cartRepositoryPort.findByDeviceId(deviceId)
                .orElseThrow(CartNotFoundException::new);
        List<CartItem> cartItems = retrieveFullCartItems(cart.getId());

        cart.setCartItems(cartItems);

        return cart;
    }

    @Transactional
    @Override
    public CartWithAllChecks updateCart(Cart clientCart) {
        Cart updatedCart = cartRepositoryPort.findByDeviceId(clientCart.getDeviceId())
                .orElseThrow(() -> new CartNotFoundException(clientCart.getDeviceId()));

        clientCart.setId(updatedCart.getId());
        List<CartItem> updatedCartItems = retrieveFullCartItems(clientCart.getId());
        List<CartItem> clientCartItems = clientCart.getCartItems();
        clientCartItems.forEach(cartItem -> {cartItem.setCartId(clientCart.getId());});

        // Items in clientCart with no id (or id not in updatedCart) → add
        List<CartItem> toAdd = clientCartItems.stream()
                .filter(client -> updatedCartItems.stream()
                        .noneMatch(updated ->
                                client.getId() != null
                                        ? updated.getId().equals(client.getId())
                                        : updated.getProductVariantId().equals(client.getProductVariantId())))
                .toList();

        // Items in updatedCart not present in clientCart → remove
        List<CartItem> toDelete = updatedCartItems.stream()
                .filter(updated -> clientCartItems.stream()
                        .noneMatch(client -> client.getId().equals(updated.getId())))
                .toList();

        // Items present in both → copy quantity and unitPrice
        updatedCartItems.stream()
                .filter(updated -> clientCartItems.stream()
                        .anyMatch(client -> client.getId().equals(updated.getId())))
                .forEach(updated -> clientCartItems.stream()
                        .filter(client -> client.getId().equals(updated.getId()))
                        .findFirst()
                        .ifPresent(match -> {
                            updated.setQuantity(match.getQuantity());
                            updated.setUnitPrice(match.getUnitPrice());
                        }));

        updatedCartItems.addAll(toAdd);
        updatedCartItems.removeAll(toDelete);

        Set<Long> variantIds = updatedCartItems.stream()
                .map(CartItem::getProductVariantId)
                .collect(Collectors.toSet());
        Set<ProductVariant> variants = productVariantRepositoryPort.findAllByIdInSet(variantIds);

        updateCurrentStock(updatedCartItems, variants);

        List<ProductVariantAvailabilityResult> variantAvailability = detectVariantAvailability(updatedCartItems, variants);

        List<CartItem> savedItems = new ArrayList<>();

        if (!variantAvailability.isEmpty()) {
            updatedCartItems.removeIf(cartItem -> variantAvailability.stream()
                    .anyMatch(result -> result.productVariantId().equals(cartItem.getProductVariantId())));
            savedItems = cartItemRepositoryPort.saveAll(updatedCartItems);
        }

        List<ProductPriceChange> priceChanges = detectPriceChanges(updatedCartItems, variants);

        List<InsufficientStockResult> insufficientStock = detectInsufficientStock(updatedCartItems);

        if (!priceChanges.isEmpty()) {
            applyPriceChanges(updatedCartItems, priceChanges);
        }
        if (!insufficientStock.isEmpty()) {
            applyQuantityChanges(updatedCartItems, insufficientStock);
        }

        cartItemRepositoryPort.saveAll(savedItems);

        updatedCart.setCartItems(updatedCartItems);

        List<Long> cartItemIds = toDelete.stream()
                .map(CartItem::getId)
                .collect(Collectors.toList());

        cartItemRepositoryPort.deleteAllByIdIn(cartItemIds);

        return new CartWithAllChecks(updatedCart, priceChanges, insufficientStock, variantAvailability);
    }


    @Transactional
    @Override
    public Cart updateCartItemsQuantities(CartItemsQuantitiesUpdateDTO cartItemsQuantitiesUpdateDTO) {

        Cart cart = findCartByDeviceId(cartItemsQuantitiesUpdateDTO.deviceId());
        List<CartItem> cartItems = cartItemRepositoryPort.findAllByCartId(cart.getId());

        if (cartItemsQuantitiesUpdateDTO.cartItemCommands() != null && !cartItemsQuantitiesUpdateDTO.cartItemCommands().isEmpty()) {
            List<UpdateCartItemQuantityCommand> itemCommands = cartItemsQuantitiesUpdateDTO.cartItemCommands();
            for (CartItem cartItem : cartItems) {
                itemCommands.stream()
                        .filter(command -> command.cartItemId().equals(cartItem.getId()))
                        .findFirst()
                        .ifPresent(command -> cartItem.setQuantity(command.quantity()));
            }
            cartItemRepositoryPort.saveAll(cartItems);
        }

        List<CartItem> updatedItems = cartItemRepositoryPort.findAllByCartId(cart.getId());
        cart.setCartItems(updatedItems);

        return cartRepositoryPort.save(cart);
    }

    private List<CartItem> retrieveFullCartItems(Long cartId) {
        List<CartItem> cartItems = cartItemRepositoryPort.findAllByCartId(cartId);

        Set<Long> variantIds = cartItems.stream()
                .map(CartItem::getProductVariantId)
                .collect(Collectors.toSet());
        Set<ProductVariant> variants = productVariantRepositoryPort.findAllByIdInSet(variantIds);

        updateCurrentStock(cartItems, variants);

        setProductIdsInItems(cartItems, variants);

        return cartItems;
    }

    private void applyPriceChanges(List<CartItem> cartItems, List<ProductPriceChange> priceChanges) {
        Map<Long, Double> variantsWithNewPriceMap = priceChanges.stream()
                .collect(Collectors.toMap(ProductPriceChange::productVariantId, ProductPriceChange:: newPrice));
        cartItems.forEach(cartItem -> {
            if (variantsWithNewPriceMap.containsKey(cartItem.getProductVariantId())) {
                cartItem.setUnitPrice(variantsWithNewPriceMap.get(cartItem.getProductVariantId()));
            }
        });
    }

    private void applyQuantityChanges(List<CartItem> cartItems, List<InsufficientStockResult> stockDifferences) {
        Map<Long, Integer> itemsWithInsufficientStock = stockDifferences.stream()
                .collect(Collectors.toMap(InsufficientStockResult::productVariantId, InsufficientStockResult::availableStock));
        cartItems.forEach(cartItem -> {
            if (itemsWithInsufficientStock.containsKey(cartItem.getProductVariantId())) {
                cartItem.setQuantity(itemsWithInsufficientStock.get(cartItem.getProductVariantId()));
            }
        });
    }

    private List<ProductPriceChange> detectPriceChanges(List<CartItem> cartItems, Set<ProductVariant> variants) {
        Set<Long> productIds = variants.stream()
                .map(ProductVariant::getProductId)
                .collect(Collectors.toSet());
        Set<Product> products = productRepositoryPort.findAllByIdInSet(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        Map<Long, Long> variantToProductId = variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, ProductVariant::getProductId));

        List<ProductPriceChange> changedPricesList = new ArrayList<>();

        cartItems.forEach(cartItem -> {
            Long productId =variantToProductId.get(cartItem.getProductVariantId());
            Product product = productMap.get(productId);
            if (!Objects.equals(cartItem.getUnitPrice(), product.getPrice())) {
                changedPricesList.add(new ProductPriceChange(
                        cartItem.getProductVariantId(),
                        cartItem.getUnitPrice(),
                        product.getPrice()
                ));
            }
        });
        return  changedPricesList;
    }

    private List<ProductVariantAvailabilityResult> detectVariantAvailability(List<CartItem> cartItems, Set<ProductVariant> variants) {

        return cartItems.stream()
                .flatMap(cartItem -> variants.stream()
                        .filter(variant -> variant.getId().equals(cartItem.getProductVariantId()))
                        .filter(variant -> !Boolean.TRUE.equals(variant.getActive()))
                        .map(variant -> ProductVariantAvailabilityResult.builder()
                                .cartItemId(cartItem.getId())
                                .productVariantId(variant.getId())
                                .isVariantAvailable(false)
                                .build()))
                .toList();
    }

    private List<InsufficientStockResult> detectInsufficientStock(List<CartItem> cartItems) {

        List<InsufficientStockResult> insufficientStockList = new ArrayList<>();

        cartItems.forEach(cartItem -> {
            if (cartItem.getQuantity() > cartItem.getCurrentStock()) {
                insufficientStockList.add(new InsufficientStockResult(
                        cartItem.getProductVariantId(),
                        cartItem.getQuantity(),
                        cartItem.getCurrentStock()
                ));
            }
        });
        return insufficientStockList;
    }

    private void updateCurrentStock(List<CartItem> cartItems, Set<ProductVariant> variants) {
        Map<Long, ProductVariant> variantMap = variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, v -> v));

        cartItems.forEach(item -> item.setCurrentStock(
                variantMap.get(item.getProductVariantId()).getStock()));
    }

    private void setProductIdsInItems(List<CartItem> cartItems, Set<ProductVariant> variants) {
        Map<Long, ProductVariant> variantMap = variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, v -> v));

        cartItems.forEach(item -> item.setProductId(
                variantMap.get(item.getProductVariantId()).getProductId()));
    }
}
