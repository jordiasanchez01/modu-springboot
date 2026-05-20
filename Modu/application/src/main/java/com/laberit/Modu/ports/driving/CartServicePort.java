package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.response.CartWithPriceAndStockCheck;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;

public interface CartServicePort {

    Cart addCartItemToCart(AddCartItemCommand command);

    CartWithPriceAndStockCheck getCartWithPriceAndStockCheck(String deviceId);

    Cart findCartByDeviceId(String deviceId);
}
