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


@RestController
@RequiredArgsConstructor
public class CartRestAdapter implements CartApi {
    private final CartServicePort cartServicePort;
    private final CartRestMapper cartMapper;
    private final CartItemServicePort cartItemServicePort;
    private final CartItemRestMapper cartItemMapper;

    @Override
    public ResponseEntity<CartResponse> getCart(String xDeviceId) {

        GetCartResponse getCartResponse = cartServicePort.findCartByUserId(Long.valueOf(xDeviceId));

        CartResponse cartResponse = cartMapper.toCartResponse(getCartResponse.cart());

        cartResponse.setPriceChangedAlert(checkIfPricesChanged(getCartResponse.changedPrices()));

        return ResponseEntity.ok(cartResponse);
    }

    @Override
    public ResponseEntity<CartResponse> addCartItem(String xDeviceId, AddItemRequest addItemRequest) {
        AddCartItemCommand addItemCommand = cartMapper.toAddCartItemCommand(
                Long.valueOf(xDeviceId),addItemRequest);

        Cart cart = cartServicePort.addCartItemToCart(addItemCommand);

        CartResponse cartResponse = cartMapper.toCartResponse(cart);

        return ResponseEntity.ok(cartResponse);
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItem(String xDeviceId, Long itemID, UpdateItemRequest updateItemRequest) {
        cartItemServicePort.updateCartItem(Long.valueOf(xDeviceId), itemID, cartItemMapper.toCommand(updateItemRequest));
        return getCart(xDeviceId);
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
