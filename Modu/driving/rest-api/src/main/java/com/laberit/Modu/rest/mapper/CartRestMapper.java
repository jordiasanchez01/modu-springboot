package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.AddItemRequest;
import com.laberit.Modu.rest.generated.model.CartItemResponse;
import com.laberit.Modu.rest.generated.model.CartResponse;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    CartResponse toCartResponse(Cart cart);

    default AddItemRequest toAddItemRequest(AddCartItemCommand addCommand) {
        return new AddItemRequest(
                addCommand.productVariantId(),
                addCommand.quantity());
    };

    default AddCartItemCommand toAddCartItemCommand(Long userId, AddItemRequest addRequest) {
        return new AddCartItemCommand(
                userId,
                addRequest.getVariantId(),
                addRequest.getQuantity()
        );
    };

    List<CartResponse>  toCartResponseList(List<Cart> carts);
}
