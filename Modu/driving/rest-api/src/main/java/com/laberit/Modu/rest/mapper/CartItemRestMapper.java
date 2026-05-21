package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import com.laberit.Modu.rest.generated.model.CartItemResponse;
import com.laberit.Modu.rest.generated.model.UpdateItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemRestMapper {

    CartItemResponse toCartItemResponse(CartItem cartItem);

    List<CartItemResponse>  toCartItemResponseList(List<CartItem> cartItemList);

    UpdateCartItemQuantityCommand toCommand(UpdateItemRequest updateItemRequest);

    List<CartItem> toCartItemList(List<CartItemResponse> cartItemResponses);
}
