package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.response.CartWithPriceCheck;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;

public interface CartServicePort {

    Cart addCartItemToCart(AddCartItemCommand command);

    CartWithPriceCheck getCartWithPriceCheck(Long userId);

    Cart findCartByUserId(Long userId);
}
