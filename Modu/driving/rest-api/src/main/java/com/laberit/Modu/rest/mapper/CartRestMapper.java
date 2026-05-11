package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.rest.generated.model.CartResponse;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    default CartResponse toCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
            cartResponse.setUserId(cart.getUserId());
            cartResponse.setCreatedAt(cart.getCreatedAt());
            cartResponse.setTotalPrice(cart.getTotalCartPrice());
        return cartResponse;
    };

    List<CartResponse>  toProductDetailsResponseList(List<Cart> carts);
}
