package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;

public interface CartItemServicePort {

    CartItem updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemQuantityCommand command);

    void deleteCartItemById(Long userId, Long cartItemId);

    void deleteAllCartItems(Long userId);

}
