package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driven.*;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.AddCartCommand;
import com.laberit.Modu.ports.driving.command.AddProductCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartCommand;
import com.laberit.Modu.ports.driving.command.UpdateProductCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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


    @Override
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
    public Cart addCart(AddCartCommand command) {
        return null;
    }

    @Override
    public Cart updateCart(UpdateCartCommand command) {
        return null;
    }

    @Override
    public void deleteCart(Long CartId) {

    }

}
