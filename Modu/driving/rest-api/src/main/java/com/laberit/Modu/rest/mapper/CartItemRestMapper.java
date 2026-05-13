package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driving.command.UpdateCartItemCommand;
import com.laberit.Modu.rest.generated.model.CartItemResponse;
import com.laberit.Modu.rest.generated.model.UpdateItemRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemRestMapper {

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse>  toCartItemResponseList(List<CartItem> cartItemList);

    UpdateCartItemCommand toCommand(UpdateItemRequest updateItemRequest);
}
