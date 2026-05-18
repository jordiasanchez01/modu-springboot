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
        CartWithPriceCheck result = cartServicePort.getCartWithPriceCheck(Long.valueOf(xDeviceId));
        return ResponseEntity.ok(cartMapper.toCartWithPriceCheckResponse(result));
    }

    @Override
    public ResponseEntity<CartResponse> addCartItem(String xDeviceId, AddItemRequest addItemRequest) {
        Cart cart = cartServicePort.addCartItemToCart(cartMapper.toAddCartItemCommand(
                Long.valueOf(xDeviceId),addItemRequest));
        return ResponseEntity.ok(cartMapper.toCartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItemQuantity(String xDeviceId, Long itemID, UpdateItemRequest updateItemRequest) {
        Cart cart = cartItemServicePort.updateCartItemQuantity(Long.valueOf(xDeviceId), itemID, cartItemMapper.toCommand(updateItemRequest));
        return ResponseEntity.ok(cartMapper.toCartResponse(cart));
    }
}
