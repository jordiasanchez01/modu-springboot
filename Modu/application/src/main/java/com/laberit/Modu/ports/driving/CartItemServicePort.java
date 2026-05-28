package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;

import java.util.Set;

public interface CartItemServicePort {

    CartItem updateCartItemQuantity(String deviceId, UpdateCartItemQuantityCommand command);

    void deleteCartItemById(String deviceId, Long cartItemId);

    void deleteAllCartItems(String deviceId);

    Cart addCartItemToCart(AddCartItemCommand command);

    Set<InsufficientStockResult> checkStockOfCartItems(Set<CartItem> cartItems);
}
