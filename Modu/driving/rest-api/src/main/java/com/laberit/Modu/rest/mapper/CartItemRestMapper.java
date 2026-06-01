package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.CartItemsQuantitiesUpdateDTO;
import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartItemRestMapper {

    CartItemForCartResponse toCartItemForCartResponse(CartItem cartItem);

    CartItemForCartUpdate toCartItemForCartUpdate(CartItem cartItem);

    CartItemForOrderRequest toCartItemForOrderRequest(CartItem cartItem);

    @Mapping(source = "id", target = "cartItemId")
    CartItemQuantityUpdateRequest toCartItemQuantityUpdateRequest(CartItem cartItem);

    List<CartItemForCartResponse>  toCartItemForCartResponseList(List<CartItem> cartItemList);

    List<CartItemForCartUpdate> toCartItemForCartUpdateList(List<CartItem> cartItemList);

    List<CartItemForOrderRequest>   toCartItemForOrderRequestList(List<CartItem> cartItemList);

    UpdateCartItemQuantityCommand toUpdateCartItemQuantityCommand(CartItemQuantityUpdateRequest updateItemRequest);

    List<UpdateCartItemQuantityCommand> toUpdateCartItemQuantityCommandList(List<CartItemQuantityUpdateRequest> updateItemRequests);

    default CartItemsQuantitiesUpdateDTO toCartItemsQuantitiesUpdateDTO(String deviceId, UpdateCartQuantitiesRequest updateCartQuantitiesRequest) {
        return CartItemsQuantitiesUpdateDTO.builder()
                .deviceId(deviceId)
                .cartItemCommands(
                        toUpdateCartItemQuantityCommandList(updateCartQuantitiesRequest.getCartItems())
                )
                .build();
    }

    @Mapping(target = "cartId", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "productVariantId", ignore = true)
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "currentStock", ignore = true)
    @Mapping(source = "cartItemId", target = "id")
    List<CartItem> toCartItemListFromQuantityUpdateRequest(List<CartItemQuantityUpdateRequest> cartItemQuantityUpdateRequests);


}
