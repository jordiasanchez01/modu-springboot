package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driven.*;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceUseCase implements CartServicePort {
    private final CartRepositoryPort cartRepositoryPort;
    private final CartItemRepositoryPort cartItemRepositoryPort;
    private final ProductVariantServicePort productVariantServicePort;
    private final ProductServicePort productServicePort;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Cart findCartByUserId(Long userId) {

        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId.toString()));

        List<CartItem> cartItems = cartItemRepositoryPort.findAllByCartId(cart.getUserId());

        Set<Long> variantIds = cartItems.stream()
                                .map(CartItem::getProductVariantId)
                                .collect(Collectors.toSet());
        Set<ProductVariant> variants = productVariantServicePort.findAllByIdIn(variantIds);

        Map<Long, ProductVariant> variantMap = variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, pV -> pV));

        cartItems.forEach(cartItem -> cartItem.setCurrentStock(
                variantMap.get(cartItem.getProductVariantId()).getStock()
        ));

        cart.setCartItems(cartItems);

        return cart;
    }

    @Override
    @Transactional
    public Cart addCartItemToCart(AddCartItemCommand addCommand) {
        Cart cart = cartRepositoryPort.findByUserId(addCommand.userId())
                .orElseGet(() -> addCart(new AddCartCommand(
                        addCommand.userId(),
                        new ArrayList<>()
                )));

        CartItem item = newCartItem(new AddCartItemCommand(
                cart.getUserId(),
                addCommand.productVariantId(),
                addCommand.quantity()
        ));

        cart.setCartItems(addItemToCartItemList(item, cart.getCartItems()));
        cartRepositoryPort.save(cart);

        return findCartByUserId(addCommand.userId());
    }

    private CartItem newCartItem(AddCartItemCommand command) {
        ProductVariant variant = productVariantServicePort.findById(command.productVariantId())
                .orElseThrow(()-> new ProductVariantNotFoundException(
                        command.productVariantId().toString()
                ));
        Double productPrice = productServicePort.findProductById(variant.getProductId()).getPrice();
        return CartItem.builder()
                .cartId(command.userId())
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

    @Override
    public Cart updateCart(UpdateCartCommand command) {
        return null;
    }

    @Override
    public void deleteCart(Long CartId) {

    }

}
