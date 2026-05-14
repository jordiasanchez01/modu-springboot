package com.laberit.Modu.repositories.mappers;

import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.repositories.models.CartEntity;
import com.laberit.Modu.repositories.models.CartItemEntity;
import com.laberit.Modu.repositories.models.ProductVariantEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel="spring")
public interface CartItemPersistanceMapper {

    default CartItemEntity toEntity(CartItem cartItem){
        if (cartItem==null){
            return null;
        }
        CartItemEntity entity = new CartItemEntity();
            CartEntity cart = new CartEntity();
            cart.setId(cartItem.getCartId());
        entity.setCart(cart);
        entity.setUnitPrice(cartItem.getUnitPrice());
        entity.setQuantity(cartItem.getQuantity());
        entity.setTotalPrice(cartItem.getTotalPrice());
            ProductVariantEntity productVariant = new ProductVariantEntity();
            productVariant.setId(cartItem.getProductVariantId());
        entity.setProductVariant(productVariant);

        return entity;
    }


    default CartItem toDomain(CartItemEntity entity){
        if (entity==null){
            return null;
        }
        return CartItem.builder()
                .id(entity.getId())
                .cartId(entity.getCart().getId())
                .productVariantId(entity.getProductVariant().getId())
                .unitPrice(entity.getUnitPrice())
                .quantity(entity.getQuantity())
                .build();


    }

    List<CartItem> toDomainList(List<CartItemEntity> entities);
    List<CartItemEntity> toEntityList(List<CartItem> cartItems);
}
