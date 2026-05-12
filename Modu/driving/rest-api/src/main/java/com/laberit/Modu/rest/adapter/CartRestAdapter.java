package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.Product;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.rest.generated.api.CartApi;
import com.laberit.Modu.rest.generated.model.*;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import com.laberit.Modu.rest.mapper.ProductRestMapper;
import com.laberit.Modu.rest.mapper.ProductVariantRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
public class CartRestAdapter implements CartApi {
    private final CartServicePort cartServicePort;
    private final CartRestMapper cartMapper;
    private final ProductVariantServicePort productVariantServicePort;

    private final ProductServicePort productServicePort;


    @Override
    public ResponseEntity<CartResponse> addCartItem(String xDeviceId, AddItemRequest addItemRequest) {
        return null;
    }

    @Override
    public ResponseEntity<CartResponse> getCart(String xDeviceId) {

        Cart cart = cartServicePort.findCartByUserId(Long.valueOf(xDeviceId));

        CartResponse cartResponse = cartMapper.toCartResponse(cart);

        cartResponse.setPriceChangedAlert(checkIfPricesChanged(cart.getCartItems()));

        return ResponseEntity.ok(cartResponse);
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItem(String xDeviceId, Long itemID, UpdateItemRequest updateItemRequest) {

        return null;
    }

    private CartResponsePriceChangedAlert checkIfPricesChanged(List<CartItem> cartItems){

        CartResponsePriceChangedAlert changedAlert = new CartResponsePriceChangedAlert();
        List<CartResponsePriceChangedAlertCartItemsInner> alertList = new ArrayList<>();

        for (CartItem item : cartItems) {
            Long varId = item.getProductVariantId();
            ProductVariant prodVar = productVariantServicePort.findById(varId)
                    .orElseThrow(() -> new ProductVariantNotFoundException(varId.toString()));
            Product product = productServicePort.findProductById(prodVar.getProductId());

            if (!Objects.equals(item.getUnitPrice(), product.getPrice())){
                CartResponsePriceChangedAlertCartItemsInner innerItem = new CartResponsePriceChangedAlertCartItemsInner(
                  prodVar.getId(), product.getPrice()
                );
                alertList.add(innerItem);
            }
        }
        if (!alertList.isEmpty()){
            changedAlert.setPriceChanged(true);
            changedAlert.setCartItems(alertList);
        }
        return changedAlert;
    }
}
