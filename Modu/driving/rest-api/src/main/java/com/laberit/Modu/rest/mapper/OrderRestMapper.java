package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.response.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import com.laberit.Modu.ports.driving.command.AddOrderItemCommand;
import com.laberit.Modu.rest.generated.model.AddItemRequest;
import com.laberit.Modu.rest.generated.model.AddOrderRequest;
import com.laberit.Modu.rest.generated.model.OrderResponse;
import com.laberit.Modu.rest.generated.model.ProductPriceChangeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderRestMapper {

    default OffsetDateTime map(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }

    @Mapping(target = "subtotalPrice", expression = "java(order.getSubTotalPrice())")
    @Mapping(target = "totalPrice", expression = "java(order.getTotalOrderPrice())")
    OrderResponse toOrderResponse(Order order);

    ProductPriceChangeResponse toProductPriceChangeResponse(ProductPriceChange priceChange);

    default AddItemRequest toAddItemRequest(AddOrderItemCommand addCommand) {
        return new AddItemRequest(
                addCommand.productVariantId(),
                addCommand.quantity());
    };

    default AddOrderItemCommand toAddOrderItemCommand(Long userId, AddItemRequest addRequest) {
        return new AddOrderItemCommand(
                userId,
                addRequest.getVariantId(),
                addRequest.getQuantity()
        );
    };

    AddOrderRequest toAddOrderRequest(AddOrderCommand command);

    AddOrderCommand toAddOrderCommand(AddOrderRequest request);

    List<OrderResponse>  toOrderResponseList(List<Order> orders);

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);
}
