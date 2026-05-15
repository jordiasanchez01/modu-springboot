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

        Optional<CartItem> existing = cartItemRepositoryPort
                .findByCartIdAndProductVariantId(
                        cart.getId(),
                        addCommand.productVariantId()
                );

        if (existing.isPresent()) {
            // Product already in cart — update quantity and total
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + addCommand.quantity());
            item.setCurrentStock(getProductVariant(addCommand.productVariantId()).getStock());
            CartItem savedItem = cartItemRepositoryPort.save(item);
        } else {
            // Product not in cart yet — insert new cart item
            CartItem cartItem = newCartItem(new AddCartItemCommand(
                    cart.getId(),
                    addCommand.productVariantId(),
                    addCommand.quantity()
            ));
            cartItemRepositoryPort.save(cartItem);
        }
        cart = findCartByUserId(addCommand.cartId()).cart();

        return cartRepositoryPort.save(cart);
    }

    @Override
    public Cart updateCart(UpdateCartCommand command) {
        return null;
    }

    @Override
    @Transactional
    public void deleteCart(Long userId) {
        if (cartRepositoryPort.existsByUserId(userId)) {
            cartRepositoryPort.deleteByUserId(userId);
        }
    }

    @Override
    public List<ProductPriceChange> checkIfPricesChanged(List<CartItem> cartItems){
        List<ProductPriceChange> changedPricesList = new ArrayList<>();

        for (CartItem item : cartItems) {
            ProductVariant prodVar = getProductVariant(item.getProductVariantId());
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
