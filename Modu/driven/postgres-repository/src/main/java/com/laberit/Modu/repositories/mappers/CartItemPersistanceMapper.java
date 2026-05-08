package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.repositories.models.CartItemEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface CartItemPersistanceMapper {

    default CartItemEntity toEntity(CartItem cartItem){
        if (cartItem==null){
            return null;
        }
        CartItemEntity entity = new CartItemEntity();
        entity.setId(cartItem.getId());
        entity.setUnitPrice(entity.getUnitPrice());
        entity.setQuantity(cartItem.getQuantity());
        entity.setTotalPrice(cartItem.getTotalPrice());

        return entity;
    }


    default CartItem toDomain(CartItemEntity entity){
        if (entity==null){
            return null;
        }
        return CartItem.builder()
                .id(entity.getId())
                .productVariantId(entity.getProductVariant().getId())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity())
                .build();


    }

    List<CartItem> toDomainList(List<CartItemEntity> entities);
    List<CartItemEntity> toEntityList(List<CartItem> cartItems);
}
