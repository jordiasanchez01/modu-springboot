package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.response.CartWithPriceCheck;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.AddItemRequest;
import com.laberit.Modu.rest.generated.model.CartResponse;
import com.laberit.Modu.rest.generated.model.ProductPriceChangeResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    default CartResponse toCartWithPriceCheckResponse(CartWithPriceCheck cartWithPriceCheck) {
        CartResponse response = toCartResponse(cartWithPriceCheck.cart());
        response.setPriceChanges(toProductPriceChangeResponseList(cartWithPriceCheck.changedPrices()));
        return response;
    }

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);

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
