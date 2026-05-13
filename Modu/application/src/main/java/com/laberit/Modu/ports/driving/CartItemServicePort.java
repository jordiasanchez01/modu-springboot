package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartItemCommand;

public interface CartItemServicePort {

    CartItem findCartItemById(Long CartItemId);

    CartItem findCartItemByName(String name);

    CartItem addCartItem(AddCartItemCommand command);

    CartItem updateCartItem(UpdateCartItemCommand command);

    void deleteCartItem(Long CartItemId);

}
