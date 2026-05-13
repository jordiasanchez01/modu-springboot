package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.*;
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

        GetCartResponse getCartResponse = cartServicePort.findCartByUserId(Long.valueOf(xDeviceId));

        CartResponse cartResponse = cartMapper.toCartResponse(getCartResponse.cart());

        cartResponse.setPriceChangedAlert(checkIfPricesChanged(getCartResponse.changedPrices()));

        return ResponseEntity.ok(cartResponse);
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItem(String xDeviceId, Long itemID, UpdateItemRequest updateItemRequest) {
        return null;
    }

    private CartResponsePriceChangedAlert checkIfPricesChanged(List<ProductPriceChange> pricesList){

        CartResponsePriceChangedAlert changedAlert = new CartResponsePriceChangedAlert();
        changedAlert.setPriceChanged(false);

        if (!pricesList.isEmpty()){
            changedAlert.setPriceChanged(true);
            changedAlert.setCartItems(cartMapper.toProductPriceChangeResponseList(pricesList));
        }
        return changedAlert;
    }
}
