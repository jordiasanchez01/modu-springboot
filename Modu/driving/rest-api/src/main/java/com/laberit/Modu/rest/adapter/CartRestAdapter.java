package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.rest.generated.api.CartApi;
import com.laberit.Modu.rest.generated.model.AddItemRequest;
import com.laberit.Modu.rest.generated.model.CartResponse;
import com.laberit.Modu.rest.generated.model.UpdateItemRequest;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CartRestAdapter implements CartApi {
    private final CartServicePort cartServicePort;
    private final CartRestMapper mapper;


    @Override
    public ResponseEntity<CartResponse> addCartItem(String xDeviceId, AddItemRequest addItemRequest) {
        return null;
    }

    @Override
    public ResponseEntity<CartResponse> getCart(String xDeviceId) {

        return ResponseEntity.ok(mapper.toCartResponse(cartServicePort.findCartByUserId(Long.valueOf(xDeviceId))));
    }

    @Override
    public ResponseEntity<CartResponse> updateCartItem(String xDeviceId, Long itemID, UpdateItemRequest updateItemRequest) {
        return null;
    }
}
