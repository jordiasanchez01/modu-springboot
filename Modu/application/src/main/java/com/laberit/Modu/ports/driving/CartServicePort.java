package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartWithPriceCheck;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartCommand;

public interface CartServicePort {

    Cart addCartItemToCart(AddCartItemCommand command);

    CartWithPriceCheck getCartWithPriceCheck(Long userId);

}
