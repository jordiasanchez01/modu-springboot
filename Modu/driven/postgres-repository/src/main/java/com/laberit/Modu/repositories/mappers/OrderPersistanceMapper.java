package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.repositories.models.OrderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface OrderPersistanceMapper {

    default OrderEntity toEntity(Order order){
        if (order==null){
            return null;
        }
        OrderEntity entity = new OrderEntity();

        entity.setId(order.getId());
        entity.setDeviceId(order.getDeviceId());
        entity.setSpecialInstructions(order.getSpecialInstructions());
        entity.setSubtotalPrice(order.getSubTotalPrice());
        entity.setShippingCosts(order.getShippingCosts());
        entity.setTotalPrice(order.getTotalOrderPrice());

        return entity;
    }

    default Order toDomain(OrderEntity entity){
        if (entity==null){
            return null;
        }
        return Order.builder()
                .id(entity.getId())
                .deviceId(entity.getDeviceId())
                .specialInstructions(entity.getSpecialInstructions())
                .createdAt(entity.getCreatedAt())
                .shippingCosts(entity.getShippingCosts())
                .build();
    }

    List<Order> toDomainList(List<OrderEntity> entities);
    List<OrderEntity> toEntityList(List<Order> orders);
}
