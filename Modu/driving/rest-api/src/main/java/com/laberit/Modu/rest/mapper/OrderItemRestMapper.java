package com.laberit.Modu.rest.mapper;

import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.rest.generated.model.OrderItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemRestMapper {

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);

    List<OrderItemResponse>  toOrderItemResponseList(List<OrderItem> orderItemList);
}
