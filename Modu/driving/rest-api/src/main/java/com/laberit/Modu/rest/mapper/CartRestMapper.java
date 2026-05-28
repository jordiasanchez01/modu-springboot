package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.CartItemsQuantitiesUpdateDTO;
import com.laberit.Modu.domain.model.response.*;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    default CartResponseAllChecks toCartWithAllChecksResponse(CartWithAllChecks cartWithAllChecks) {
        CartResponseAllChecks response = new CartResponseAllChecks();

        response.deviceId(cartWithAllChecks.cart().getDeviceId());
        response.createdAt(cartWithAllChecks.cart().getCreatedAt());
        response.updatedAt(cartWithAllChecks.cart().getUpdatedAt());
        List<CartItemForCartResponse> responseItemList = toCartItemForCartResponseList(cartWithAllChecks.cart().getCartItems());
        response.setCartItems(responseItemList);

        List<ProductPriceChangeResponse> priceChangeList = toProductPriceChangeResponseList(cartWithAllChecks.changedPrices());
        PriceChangedAlert priceChangedAlert = new PriceChangedAlert();
        priceChangedAlert.setCartItems(priceChangeList);
        response.setPriceChangedAlert(priceChangedAlert);

        List<InsufficientStockResponse> insufficientStockList = toInsufficientStockResponseList(cartWithAllChecks.insufficientStock());
        InsufficientStockAlert insufficientStockAlert = new InsufficientStockAlert();
        insufficientStockAlert.setCartItems(insufficientStockList);
        response.setInsufficientStockAlert(insufficientStockAlert);

        List<ProductVariantAvailabilityResponse> variantAvailabilityList = toVariantAvailabilityResponseList(cartWithAllChecks.unavailableVariants());
        VariantAvailabilityAlert variantAvailabilityAlert = new VariantAvailabilityAlert();
        variantAvailabilityAlert.setCartItems(variantAvailabilityList);
        response.setVariantAvailabilityAlert(variantAvailabilityAlert);

        return response;
    }

    Cart toCartFromCartUpdateRequest(CartUpdateRequest cartUpdateRequest);

    CartResponse toCartResponse(Cart cart);

    CartResponseAllChecks toCartResponseAllChecks(CartWithAllChecks cartWithAllChecks);

    CartUpdateRequest toCartUpdateRequest(Cart cart);

    CartUpdatedAtResponse toCartUpdatedAtResponse(LocalDateTime updatedAt);

    default AddItemRequest toAddItemRequest(AddCartItemCommand addCommand) {
        return new AddItemRequest(
                addCommand.productVariantId(),
                addCommand.quantity());
    }

    @Mapping(target = "cartSummary", source = "cart")
    @Mapping(target = "priceChangedAlert.cartItems", source = "changedPrices")
    @Mapping(target = "insufficientStockAlert.cartItems", source = "insufficientStock")
    @Mapping(target = "variantAvailabilityAlert.cartItems", source = "unavailableVariants")
    ValidatedCartResponse toValidatedCartResponse(CartWithAllChecks cartWithAllChecks);

    default AddCartItemCommand toAddCartItemCommand(String deviceId, AddItemRequest addRequest) {
        return new AddCartItemCommand(
                deviceId,
                addRequest.getVariantId(),
                addRequest.getQuantity()
        );
    }

    default CartItemsQuantitiesUpdateDTO toCartItemsQuantitiesUpdateDTO(String deviceId, UpdateCartQuantitiesRequest updateCartQuantitiesRequest) {
        return CartItemsQuantitiesUpdateDTO.builder()
                .deviceId(deviceId)
                .cartItemCommands(null)
                .build();
    }

    List<CartItemForCartResponse>  toCartItemForCartResponseList(List<CartItem> cartItemList);

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);

    List<InsufficientStockResponse> toInsufficientStockResponseList(List<InsufficientStockResult> insufficientStocks);

    List<ProductVariantAvailabilityResponse> toVariantAvailabilityResponseList(List<ProductVariantAvailabilityResult> unavailableVariants);
}
