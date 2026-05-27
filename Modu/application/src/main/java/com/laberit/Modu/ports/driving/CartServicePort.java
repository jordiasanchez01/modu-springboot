package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItemsQuantitiesUpdateDTO;
import com.laberit.Modu.domain.model.response.CartWithAllChecks;

import java.time.LocalDateTime;

public interface CartServicePort {

    CartWithAllChecks getCartWithAllChecks(String deviceId);

    LocalDateTime getCartUpdatedAt(String deviceId);

    Cart findCartByDeviceId(String deviceId);

    Cart updateCartItemsQuantities(String deviceId, CartItemsQuantitiesUpdateDTO cartItemsQuantitiesUpdateDTO);

    CartWithAllChecks updateCart(Cart cart);

    Cart initializeCart(String deviceId);
}
