package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driven.*;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    private final ProductVariantServicePort productVariantServicePort;
    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public GetCartResponse findCartByUserId(Long userId) {

        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> cartItems = cartItemRepositoryPort.findAllByCartId(cart.getId());

        Set<Long> variantIds = cartItems.stream()
                .map(CartItem::getProductVariantId)
                .collect(Collectors.toSet());

        Set<ProductVariant> variants = productVariantRepositoryPort.findAllByIdInSet(variantIds);

        Set<Long> productIds = variants.stream()
                .map(ProductVariant::getProductId)
                .collect(Collectors.toSet());

        Set<Product> products = productRepositoryPort.findAllByIdInSet(productIds);

        Map<Long, ProductVariant> variantMap = variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, pV -> pV));

        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<ProductPriceChange> changedPricesList = new ArrayList<>();

        cartItems.forEach(cartItem -> {
            ProductVariant variant = variantMap.get(cartItem.getProductVariantId());
            Product product = productMap.get(variant.getProductId());

            cartItem.setCurrentStock(variant.getStock());

            if (!Objects.equals(cartItem.getUnitPrice(), product.getPrice())) {
                changedPricesList.add(new ProductPriceChange(
                        variant.getId(),
                        cartItem.getUnitPrice(),
                        product.getPrice()
                ));
                cartItem.setUnitPrice(product.getPrice());
            }
        });

        cartItemRepositoryPort.saveAll(cartItems);

        cart.setCartItems(cartItems);

        return new GetCartResponse(cart, changedPricesList);
    }

    @Override
    @Transactional
    public Cart addCartItemToCart(AddCartItemCommand addCommand) {
        Cart cart = cartRepositoryPort.findByUserId(addCommand.cartId())
                .orElseGet(() -> addCart(new AddCartCommand(
                        addCommand.cartId(),
                        new ArrayList<>()
                )));

        List<CartItem> cartItems = cartItemRepositoryPort.findAllByCartId(cart.getId());

        Optional<CartItem> existing = cartItemRepositoryPort
                .findByCartIdAndProductVariantId(
                        cart.getId(),
                        addCommand.productVariantId()
                );

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + addCommand.quantity());
            item.setCurrentStock(getProductVariant(addCommand.productVariantId()).getStock());
            CartItem savedItem = cartItemRepositoryPort.save(item);
            cartItems.replaceAll(ci -> ci.getId().equals(savedItem.getId()) ? savedItem : ci);
        } else {
            CartItem cartItem = newCartItem(new AddCartItemCommand(
                    cart.getId(),
                    addCommand.productVariantId(),
                    addCommand.quantity()
            ));
            CartItem savedItem = cartItemRepositoryPort.save(cartItem);
            cartItems.add(savedItem);
        }

        cart.setCartItems(cartItems);

        return cartRepositoryPort.save(cart);
    }

    @Override
    public Cart updateCart(UpdateCartCommand command) {
        return null;
    }

    private CartItem newCartItem(AddCartItemCommand command) {
        ProductVariant variant = getProductVariant(command.productVariantId());
        Product product = productRepositoryPort.findById(variant.getProductId())
                .orElseThrow(()->new ProductNotFoundException(
                        variant.getProductId().toString()
                ));
        Double productPrice = product.getPrice();

        return CartItem.builder()
                .cartId(command.cartId())
                .productVariantId(command.productVariantId())
                .unitPrice(productPrice)
                .quantity(command.quantity())
                .currentStock(variant.getStock())
                .build();
    }

    private List<CartItem> addItemToCartItemList(CartItem newItem, List<CartItem> itemList){
        for (CartItem item : itemList){
            if (item.getProductVariantId().equals(newItem.getProductVariantId())) {
                Integer amount = item.getQuantity() + newItem.getQuantity();
                item.setQuantity(amount);
                return itemList;
            }
        }
        itemList.add(newItem);
        return itemList;
    }


    private Cart addCart(AddCartCommand command) {
        Cart newCart = Cart.builder()
                .userId(command.cartId())
                .cartItems(command.cartItems())
                .build();
        return cartRepositoryPort.save(newCart);
    }

    private ProductVariant getProductVariant(Long id) {
        return productVariantRepositoryPort.findById(id)
                .orElseThrow(()-> new ProductVariantNotFoundException(
                        id.toString()
                ));
    }
}
