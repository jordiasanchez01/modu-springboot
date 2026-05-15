package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.GetCartResponse;
import com.laberit.Modu.domain.model.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartCommand;

import java.util.List;

public interface CartServicePort {

    GetCartResponse findCartByUserId(Long userId);

    List<ProductPriceChange> checkIfPricesChanged(List<CartItem> cartItems);

    Cart addCartItemToCart(AddCartItemCommand command);

    Cart updateCart(UpdateCartCommand command);
}
