package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.rest.generated.model.CartItemResponse;
import com.laberit.Modu.rest.generated.model.CartResponse;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    CartResponse toCartResponse(Cart cart);

    List<CartResponse>  toCartResponseList(List<Cart> carts);
}
