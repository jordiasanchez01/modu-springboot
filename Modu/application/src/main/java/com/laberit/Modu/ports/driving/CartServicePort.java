package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartWithPriceCheck;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartCommand;

public interface CartServicePort {

    CartWithPriceCheck findCartByUserId(Long userId);

    Cart addCartItemToCart(AddCartItemCommand command);

    Cart updateCart(UpdateCartCommand command);

    void deleteCart(Long CartId);

    CartWithPriceCheck getCartWithPriceCheck(Long userId);

}
