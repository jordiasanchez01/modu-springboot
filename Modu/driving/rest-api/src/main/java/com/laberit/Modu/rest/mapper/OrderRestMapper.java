package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.ProductPriceChange;
import com.laberit.Modu.ports.driving.command.AddOrderItemCommand;
import com.laberit.Modu.rest.generated.model.AddItemRequest;
import com.laberit.Modu.rest.generated.model.OrderResponse;
import com.laberit.Modu.rest.generated.model.ProductPriceChangeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderRestMapper {

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

    List<OrderResponse>  toOrderResponseList(List<Order> orders);

    List<ProductPriceChangeResponse> toProductPriceChangeResponseList(List<ProductPriceChange> prices);
}
