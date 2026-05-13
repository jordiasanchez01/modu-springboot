package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.rest.generated.model.CartItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemRestMapper {

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse>  toCartItemResponseList(List<CartItem> cartItemList);
}
