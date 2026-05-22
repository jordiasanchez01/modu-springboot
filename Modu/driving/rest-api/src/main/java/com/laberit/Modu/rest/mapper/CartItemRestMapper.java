package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import com.laberit.Modu.rest.generated.model.CartItemForCartResponse;
import com.laberit.Modu.rest.generated.model.CartItemForOrderRequest;
import com.laberit.Modu.rest.generated.model.UpdateItemRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemRestMapper {

    CartItemForCartResponse toCartItemForCartResponse(CartItem cartItem);

    CartItemForOrderRequest toCartItemForOrderRequest(CartItem cartItem);

    List<CartItemForCartResponse>  toCartItemForCartResponseList(List<CartItem> cartItemList);

    List<CartItemForOrderRequest>   toCartItemForOrderRequestList(List<CartItem> cartItemList);

    UpdateCartItemQuantityCommand toCommand(UpdateItemRequest updateItemRequest);

    List<CartItem> toCartItemList(List<CartItemForCartResponse> cartItemResponses);

}
