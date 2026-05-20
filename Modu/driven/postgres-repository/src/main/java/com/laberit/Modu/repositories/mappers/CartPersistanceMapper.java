package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.repositories.models.CartEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface CartPersistanceMapper {

    default CartEntity toEntity(Cart cart){
        if (cart==null){
            return null;
        }
        CartEntity entity = new CartEntity();
        entity.setId(cart.getId());
        entity.setDeviceId(cart.getDeviceId());

        return entity;
    }


    default Cart toDomain(CartEntity entity){
        if (entity==null){
            return null;
        }
        return Cart.builder()
                .id(entity.getId())
                .deviceId(entity.getDeviceId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();


    }

    List<Cart> toDomainList(List<CartEntity> entities);
    List<CartEntity> toEntityList(List<Cart> carts);
}
