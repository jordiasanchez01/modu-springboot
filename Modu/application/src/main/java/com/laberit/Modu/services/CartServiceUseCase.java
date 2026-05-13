package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
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


    @Override
    public GetCartResponse findCartByUserId(Long userId) {

        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId.toString()));

        List<CartItem> cartItems = cartItemRepositoryPort.findAllByCartId(cart.getUserId());

        List<ProductPriceChange> changedPricesList = new ArrayList<>();

        for (CartItem item : cartItems) {
            Long varId = item.getProductVariantId();
            ProductVariant prodVar = productVariantRepositoryPort.findById(varId)
                    .orElseThrow(() -> new ProductVariantNotFoundException(varId.toString()));
            Product product = productRepositoryPort.findById(prodVar.getProductId())
                    .orElseThrow(()-> new ProductNotFoundException(prodVar.getProductId().toString()));

            if (!Objects.equals(item.getUnitPrice(), product.getPrice())){
                ProductPriceChange changedPrices = new ProductPriceChange(
                        prodVar.getId(),
                        item.getUnitPrice(),
                        product.getPrice()
                );
                changedPricesList.add(changedPrices);

                item.setUnitPrice(product.getPrice());
            }
        }

        Set<Long> variantIds = cartItems.stream()
                                .map(CartItem::getProductVariantId)
                                .collect(Collectors.toSet());
        Set<ProductVariant> variants = productVariantRepositoryPort.findAllByIdIn(variantIds);

        Map<Long, ProductVariant> variantMap = variants.stream()
                .collect(Collectors.toMap(ProductVariant::getId, pV -> pV));

        cartItems.forEach(cartItem -> cartItem.setCurrentStock(
                variantMap.get(cartItem.getProductVariantId()).getStock()
        ));

        cart.setCartItems(cartItems);

        return new GetCartResponse(cart, changedPricesList);
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

    @Override
    public List<ProductPriceChange> checkIfPricesChanged(List<CartItem> cartItems){
        List<ProductPriceChange> changedPricesList = new ArrayList<>();

        for (CartItem item : cartItems) {
            Long varId = item.getProductVariantId();
            ProductVariant prodVar = productVariantRepositoryPort.findById(varId)
                    .orElseThrow(() -> new ProductVariantNotFoundException(varId.toString()));
            Product product = productRepositoryPort.findById(prodVar.getProductId())
                    .orElseThrow(()-> new ProductNotFoundException(prodVar.getProductId().toString()));

            if (!Objects.equals(item.getUnitPrice(), product.getPrice())){
                ProductPriceChange changedPrices = new ProductPriceChange(
                        prodVar.getId(),
                        item.getUnitPrice(),
                        product.getPrice()
                );
                changedPricesList.add(changedPrices);
            }
        }
        return changedPricesList;
    }

}
