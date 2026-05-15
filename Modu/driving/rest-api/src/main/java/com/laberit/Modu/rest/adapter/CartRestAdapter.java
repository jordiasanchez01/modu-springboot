package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.api.CartApi;
import com.laberit.Modu.rest.generated.model.*;
import com.laberit.Modu.rest.mapper.CartItemRestMapper;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@RestController
@RequiredArgsConstructor
public class CartRestAdapter implements CartApi {
    private final CartServicePort cartServicePort;
    private final CartRestMapper cartMapper;
    private final CartItemServicePort cartItemServicePort;
    private final CartItemRestMapper cartItemMapper;

    @Override
    public ResponseEntity<CartResponse> getCart(String xDeviceId) {
        boolean isPriceAlertNeeded = true;
        return ResponseEntity.ok(buildCartResponse(xDeviceId, isPriceAlertNeeded));
    }

    @Override
    public ResponseEntity<CartResponse> addCartItem(String xDeviceId, AddItemRequest addItemRequest) {
        boolean isPriceAlertNeeded = false;
        return ResponseEntity.ok(buildCartResponse(xDeviceId, isPriceAlertNeeded));
    }

    @Override
    public ResponseEntity<Void> deleteCart(String xDeviceId) {
        cartServicePort.deleteCart(Long.valueOf(xDeviceId));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItem(String xDeviceId, Long itemId, UpdateItemRequest updateItemRequest) {
        cartItemServicePort.updateCartItem(Long.valueOf(xDeviceId), itemId, cartItemMapper.toCommand(updateItemRequest));
        boolean isPriceAlertNeeded = true;
        return ResponseEntity.ok(buildCartResponse(xDeviceId, isPriceAlertNeeded));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItem(String xDeviceId, Long itemId) {
        cartItemServicePort.deleteCartItemById(Long.valueOf(xDeviceId), itemId);
        boolean isPriceAlertNeeded = true;
        return ResponseEntity.ok(buildCartResponse(xDeviceId, isPriceAlertNeeded));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItems(String xDeviceId) {
        return null;
    }

    private CartResponse buildCartResponse(String xDeviceId, boolean isPriceAlertNeeded) {
        GetCartResponse getCartResponse = cartServicePort.findCartByUserId(Long.valueOf(xDeviceId));
        CartResponse cartResponse = cartMapper.toCartResponse(getCartResponse.cart());
        if (isPriceAlertNeeded) cartResponse.setPriceChangedAlert(
                checkIfPricesChanged(getCartResponse.changedPrices()));
        return cartResponse;
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
