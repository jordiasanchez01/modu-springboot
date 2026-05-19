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
        entity.setUserId(order.getUserId());
        entity.setSpecialInstructions(order.getSpecialInstructions());
        entity.setTotalPrice(order.getTotalOrderPrice());

        return entity;
    }

    default Order toDomain(OrderEntity entity){
        if (entity==null){
            return null;
        }
        return Order.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .specialInstructions(entity.getSpecialInstructions())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    List<Order> toDomainList(List<OrderEntity> entities);
    List<OrderEntity> toEntityList(List<Order> orders);
}
