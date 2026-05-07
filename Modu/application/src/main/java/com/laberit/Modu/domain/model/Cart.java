package com.laberit.Modu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long userId;
    private Date createdAt;
    private List<CartItem> cartItems;

    public Double getTotalOrderPrice(){
        Double sum = 0.0;
        if (cartItems.isEmpty())
            return sum;
        for (CartItem item : cartItems) { sum = sum + item.getTotalPrice();};
        return sum;
    }
}
