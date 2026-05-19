package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.domain.model.response.GetCartResponse;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
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

        cartResponse.setInsufficientStockAlert(checkIfInsufficientStock(getCartResponse.insufficientStock()));

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
    public ResponseEntity<CartResponse> updateCartItemQuantity(String xDeviceId, Long itemId, UpdateItemRequest updateItemRequest) {
        cartItemServicePort.updateCartItemQuantity(Long.valueOf(xDeviceId), itemId, cartItemMapper.toCommand(updateItemRequest));
        return ResponseEntity.ok(buildCartResponse(xDeviceId));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItem(String xDeviceId, Long itemId) {
        cartItemServicePort.deleteCartItemById(Long.valueOf(xDeviceId), itemId);
        return ResponseEntity.ok(buildCartResponse(xDeviceId));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItems(String xDeviceId) {
        cartItemServicePort.deleteAllCartItems(Long.valueOf(xDeviceId));
        return ResponseEntity.ok(buildCartResponse(xDeviceId));
    }

    private CartResponse buildCartResponse(String xDeviceId) {
        GetCartResponse getCartResponse = cartServicePort.findCartByUserId(Long.valueOf(xDeviceId));
        return cartMapper.toCartResponse(getCartResponse.cart());
    }

    private PriceChangedAlert checkIfPricesChanged(List<ProductPriceChange> pricesList){

        PriceChangedAlert changedAlert = new PriceChangedAlert();
        if (!pricesList.isEmpty()){
            changedAlert.setCartItems(cartMapper.toProductPriceChangeResponseList(pricesList));
        }
        return changedAlert;
    }

    private InsufficientStockAlert checkIfInsufficientStock(List<InsufficientStockResult> stockList){

        InsufficientStockAlert  insufficientStockAlert = new InsufficientStockAlert();

        if (!stockList.isEmpty()){
            insufficientStockAlert.setCartItems(cartMapper.toInsufficientStockResponseList(stockList));
        }
        return insufficientStockAlert;
    }
}
