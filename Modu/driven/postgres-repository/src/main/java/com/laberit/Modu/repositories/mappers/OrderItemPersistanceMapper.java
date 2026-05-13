package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.OrderItem;
import com.laberit.Modu.repositories.models.OrderItemEntity;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface OrderItemPersistanceMapper {

    default OrderItemEntity toEntity(OrderItem orderItem){
        if (orderItem==null){
            return null;
        }
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(orderItem.getId());
            ProductVariantEntity productVariant = new ProductVariantEntity();
            productVariant.setId(orderItem.getProductVariantId());
        entity.setProductVariant(productVariant);
        entity.setUnitPrice(orderItem.getUnitPrice());
        entity.setQuantity(orderItem.getQuantity());
        entity.setTotalPrice(orderItem.getTotalPrice());
        return entity;
    }


    default OrderItem toDomain(OrderItemEntity entity){
        if (entity==null){
            return null;
        }
        return OrderItem.builder()
                .id(entity.getId())
                .productVariantId(entity.getProductVariant().getId())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity())
                .build();


    }

    List<OrderItem> toDomainList(List<OrderItemEntity> entities);
    List<OrderItemEntity> toEntityList(List<OrderItem> orderItems);
}
