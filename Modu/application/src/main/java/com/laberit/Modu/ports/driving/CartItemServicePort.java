package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driving.command.UpdateCartItemCommand;

public interface CartItemServicePort {

    CartItem updateCartItem(Long userId, Long cartItemId, UpdateCartItemCommand command);

    void deleteCartItemById(Long userId, Long cartItemId);

    void deleteAllCartItems(Long userId);

}
