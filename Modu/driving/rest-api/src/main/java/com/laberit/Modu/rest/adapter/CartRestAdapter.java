package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.domain.model.response.CartWithPriceAndStockCheck;
import com.laberit.Modu.ports.driving.CartItemServicePort;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.rest.generated.api.CartApi;
import com.laberit.Modu.rest.generated.model.*;
import com.laberit.Modu.rest.mapper.CartItemRestMapper;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CartRestAdapter implements CartApi {
    private final CartServicePort cartServicePort;
    private final CartRestMapper cartMapper;
    private final CartItemServicePort cartItemServicePort;
    private final CartItemRestMapper cartItemMapper;
    private final HttpServletRequest request;

    @Override
    public ResponseEntity<ValidatedCartResponse> getValidatedCart() {
        String deviceId = currentDeviceId();
        CartWithPriceAndStockCheck result = cartServicePort.getCartWithPriceAndStockCheck(deviceId);
        return ResponseEntity.ok(cartMapper.toValidatedCartResponse(result));
    }

    @Override
    public ResponseEntity<CartResponse> updateCart(UpdateCartRequest updateCartRequest) {
        String deviceId = currentDeviceId();
        Cart cart = cartServicePort.updateCart(cartMapper.toCartDTO(deviceId, updateCartRequest));
        return ResponseEntity.ok(cartMapper.toCartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> addCartItem(AddItemRequest addItemRequest) {
        String deviceId = currentDeviceId();
        Cart cart = cartItemServicePort.addCartItemToCart(cartMapper.toAddCartItemCommand(
                deviceId,addItemRequest));
        return ResponseEntity.ok(cartMapper.toCartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> initializeCart() {
        String deviceId = currentDeviceId();
        Cart cart = cartServicePort.initializeCart(deviceId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartMapper.toCartResponse(cart));
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItemQuantity(Long itemID, UpdateItemRequest updateItemRequest) {
        String deviceId = currentDeviceId();
        cartItemServicePort.updateCartItemQuantity(deviceId, itemID, cartItemMapper.toCommand(updateItemRequest));
        return ResponseEntity.ok(cartMapper.toCartResponse(cartServicePort.findCartByDeviceId(deviceId)));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItem(Long itemId) {
        String deviceId = currentDeviceId();
        cartItemServicePort.deleteCartItemById(deviceId, itemId);
        return ResponseEntity.ok(cartMapper.toCartResponse(cartServicePort.findCartByDeviceId(deviceId)));
    }

    @Override
    public ResponseEntity<CartResponse> deleteCartItems() {
        String deviceId = currentDeviceId();
        cartItemServicePort.deleteAllCartItems(deviceId);
        return ResponseEntity.ok(cartMapper.toCartResponse(cartServicePort.findCartByDeviceId(deviceId)));
    }

    @Override
    public ResponseEntity<CartUpdatedAtResponse> getCartUpdatedAt() {
        String deviceId = currentDeviceId();
        return ResponseEntity.ok(cartMapper
                .toCartUpdatedAtResponse(cartServicePort.getCartUpdatedAt(deviceId))
        );
    }

    private String currentDeviceId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
