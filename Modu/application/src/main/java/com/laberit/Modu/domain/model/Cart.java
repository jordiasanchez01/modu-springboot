package com.laberit.Modu.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    @Builder.Default
    private List<CartItem> cartItems = new ArrayList<>();

    public Double getTotalPrice(){
        return cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
}
