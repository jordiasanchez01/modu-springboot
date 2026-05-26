package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import com.laberit.Modu.rest.generated.model.CartItemForCartResponse;
import com.laberit.Modu.rest.generated.model.CartItemForOrderRequest;
import com.laberit.Modu.rest.generated.model.CartItemQuantityUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemRestMapper {

    CartItemForCartResponse toCartItemForCartResponse(CartItem cartItem);

    CartItemForOrderRequest toCartItemForOrderRequest(CartItem cartItem);

    @Mapping(source = "id", target = "cartItemId")
    CartItemQuantityUpdateRequest toCartItemQuantityUpdateRequest(CartItem cartItem);

    List<CartItemForCartResponse>  toCartItemForCartResponseList(List<CartItem> cartItemList);

    List<CartItemForOrderRequest>   toCartItemForOrderRequestList(List<CartItem> cartItemList);

    UpdateCartItemQuantityCommand toUpdateCartItemQuantityCommand(CartItemQuantityUpdateRequest updateItemRequest);

    List<UpdateCartItemQuantityCommand> toUpdateCartItemQuantityCommandList(List<CartItemQuantityUpdateRequest> updateItemRequests);

    List<CartItem> toCartItemFromCartItemResponseList(List<CartItemForCartResponse> cartItemResponses);

    @Mapping(source = "cartItemId", target = "id")
    List<CartItem> toCartItemListFromQuantityUpdateRequest(List<CartItemQuantityUpdateRequest> cartItemQuantityUpdateRequests);


}
