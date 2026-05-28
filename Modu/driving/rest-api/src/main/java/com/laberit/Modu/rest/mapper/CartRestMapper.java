package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.CartItemsQuantitiesUpdateDTO;
import com.laberit.Modu.domain.model.response.*;
import com.laberit.Modu.ports.driving.command.AddCartItemCommand;
import com.laberit.Modu.rest.generated.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CartRestMapper {

    Cart toCartFromCartUpdateRequest(CartUpdateRequest cartUpdateRequest);

    default OffsetDateTime map(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }

    @Mapping(target = "subTotalPrice", expression = "java(cart.getSubTotalPrice())")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartResponse toCartResponse(Cart cart);

    CartUpdateRequest toCartUpdateRequest(Cart cart);

    CartUpdatedAtResponse toCartUpdatedAtResponse(Instant updatedAt);

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

    List<CartItemForCartResponse>  toCartItemForCartResponseList(List<CartItem> cartItemList);

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);

    List<InsufficientStockResponse> toInsufficientStockResponseList(List<InsufficientStockResult> insufficientStocks);

    List<ProductVariantAvailabilityResponse> toVariantAvailabilityResponseList(List<ProductVariantAvailabilityResult> unavailableVariants);
}
