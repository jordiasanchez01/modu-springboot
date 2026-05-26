package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartDTO;
import com.laberit.Modu.domain.model.response.CartWithPriceAndStockCheck;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    CartResponse toCartResponse(Cart cart);

    @Mapping(target = "cartSummary", source = "cart")
    @Mapping(target = "priceChangedAlert.cartItems", source = "changedPrices")
    @Mapping(target = "insufficientStockAlert.cartItems", source = "insufficientStock")
    ValidatedCartResponse toValidatedCartResponse(CartWithPriceAndStockCheck cartWithPriceAndStockCheck);

    CartUpdatedAtResponse toCartUpdatedAtResponse(LocalDateTime updatedAt); //TODO:check if works

    default AddCartItemCommand toAddCartItemCommand(String deviceId, AddItemRequest addRequest) {
        return new AddCartItemCommand(
                deviceId,
                addRequest.getVariantId(),
                addRequest.getQuantity()
        );
    }

    default CartDTO toCartDTO(String deviceID, UpdateCartRequest updateCartRequest) {
        return CartDTO.builder()
                .deviceId(deviceID)
                .cartItems(null)
                .build();
    }

    List<CartResponse>  toCartResponseList(List<Cart> carts);

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);

    List<InsufficientStockResponse> toInsufficientStockResponseList(List<InsufficientStockResult> insufficientStocks);
}
