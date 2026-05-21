package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartDTO;
import com.laberit.Modu.domain.model.response.CartWithPriceAndStockCheck;
import com.laberit.Modu.domain.model.response.InsufficientStockResult;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    default CartResponse toCartWithPriceAndStockCheckResponse(CartWithPriceAndStockCheck cartWithPriceAndStockCheck) {
        CartResponse response = toCartResponse(cartWithPriceAndStockCheck.cart());

        List<ProductPriceChangeResponse> priceChangeList = toProductPriceChangeResponseList(cartWithPriceAndStockCheck.changedPrices());
        PriceChangedAlert priceChangedAlert = new PriceChangedAlert();
        priceChangedAlert.setCartItems(priceChangeList);
        response.setPriceChangedAlert(priceChangedAlert);

        List<InsufficientStockResponse> insufficientStockList = toInsufficientStockResponseList(cartWithPriceAndStockCheck.insufficientStock());
        InsufficientStockAlert insufficientStockAlert = new InsufficientStockAlert();
        insufficientStockAlert.setCartItems(insufficientStockList);
        response.setInsufficientStockAlert(insufficientStockAlert);

        return response;
    }

    CartResponse toCartResponse(Cart cart);

        default AddItemRequest toAddItemRequest(AddCartItemCommand addCommand) {
        return new AddItemRequest(
                addCommand.productVariantId(),
                addCommand.quantity());
    };

    default AddCartItemCommand toAddCartItemCommand(String deviceId, AddItemRequest addRequest) {
        return new AddCartItemCommand(
                deviceId,
                addRequest.getVariantId(),
                addRequest.getQuantity()
        );
    };

    default CartDTO toCartDTO(UpdateCartRequest updateCartRequest) {
        return CartDTO.builder()
                .deviceId(updateCartRequest.getDeviceId())
                .cartItems(null)
                .build();
    }

    List<CartResponse>  toCartResponseList(List<Cart> carts);

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);

    List<InsufficientStockResponse> toInsufficientStockResponseList(List<InsufficientStockResult> insufficientStocks);
}
