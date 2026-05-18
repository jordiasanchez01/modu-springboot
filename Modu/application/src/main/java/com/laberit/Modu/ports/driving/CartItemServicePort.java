package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;

public interface CartItemServicePort {

    Cart updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemQuantityCommand command);
    Cart deleteCartItemById(Long userId, Long cartItemId);
}
