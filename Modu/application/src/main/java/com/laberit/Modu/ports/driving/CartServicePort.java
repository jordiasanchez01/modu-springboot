package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItemsQuantitiesUpdateDTO;
import com.laberit.Modu.domain.model.response.CartWithAllChecks;
import com.laberit.Modu.domain.model.response.CartWithPriceAndStockCheck;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;

import java.time.LocalDateTime;

public interface CartServicePort {

    Cart addCartItemToCart(AddCartItemCommand command);

    CartWithPriceAndStockCheck getCartWithPriceAndStockCheck(String deviceId);

    LocalDateTime getCartUpdatedAt(String deviceId);

    Cart findCartByDeviceId(String deviceId);

    Cart updateCartItemsQuantities(CartItemsQuantitiesUpdateDTO cartItemsQuantitiesUpdateDTO);

    CartWithAllChecks updateCart(Cart cart);
}
