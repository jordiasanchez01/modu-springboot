package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.domain.model.response.CartWithPriceAndStockCheck;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driving.CartServicePort;
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
    public ResponseEntity<CartResponse> getValidatedCart(String xDeviceId) {
        CartWithPriceAndStockCheck result = cartServicePort.getCartWithPriceAndStockCheck(Long.valueOf(xDeviceId));
        return ResponseEntity.ok(cartMapper.toCartWithPriceAndStockCheckResponse(result));
    }

    @Override
    public ResponseEntity<CartResponse> updateCart(String xDeviceId, UpdateCartRequest updateCartRequest) {
        List<CartItem> items = cartItemMapper.toCartItemList(updateCartRequest.getCartItems());
        CartDTO cartDTO = CartDTO.builder()
                .userId(updateCartRequest.getUserId())
                .createdAt(null)
                .updatedAt(null)
                .cartItems(items)
                .build();
        Cart cart = cartServicePort.updateCart(cartDTO);
        return ResponseEntity.ok(cartMapper.toCartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> addCartItem(String xDeviceId, AddItemRequest addItemRequest) {
        Cart cart = cartServicePort.addCartItemToCart(cartMapper.toAddCartItemCommand(
                Long.valueOf(xDeviceId),addItemRequest));
        return ResponseEntity.ok(cartMapper.toCartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItemQuantity(String xDeviceId, Long itemID, UpdateItemRequest updateItemRequest) {
        cartItemServicePort.updateCartItemQuantity(Long.valueOf(xDeviceId), itemID, cartItemMapper.toCommand(updateItemRequest));
        return ResponseEntity.ok(cartMapper.toCartResponse(cartServicePort.findCartByUserId(Long.valueOf(xDeviceId))));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItem(String xDeviceId, Long itemId) {
        cartItemServicePort.deleteCartItemById(Long.valueOf(xDeviceId), itemId);
        return ResponseEntity.ok(cartMapper.toCartResponse(cartServicePort.findCartByUserId(Long.valueOf(xDeviceId))));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItems(String xDeviceId) {
        cartItemServicePort.deleteAllCartItems(Long.valueOf(xDeviceId));
        return ResponseEntity.ok(cartMapper.toCartResponse(cartServicePort.findCartByUserId(Long.valueOf(xDeviceId))));
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
