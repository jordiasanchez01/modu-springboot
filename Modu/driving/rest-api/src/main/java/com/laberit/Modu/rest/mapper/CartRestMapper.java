package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.response.GetCartResponse;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.AddItemRequest;
import com.laberit.Modu.rest.generated.model.CartResponse;
import com.laberit.Modu.rest.generated.model.InsufficientStockResponse;
import com.laberit.Modu.rest.generated.model.ProductPriceChangeResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    CartResponse toCartResponse(Cart cart);

    GetCartResponse toGetCartResponse(Cart cart);

    ProductPriceChangeResponse toProductPriceChangeResponse(ProductPriceChange priceChange);

    InsufficientStockResponse toInsufficientStockResponse(InsufficientStockResult insufficientStockResult);

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

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);

    List<InsufficientStockResponse> toInsufficientStockResponseList(List<InsufficientStockResult> insufficientStocks);
}
