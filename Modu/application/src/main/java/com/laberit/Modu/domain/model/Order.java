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
public class Order {
    private Long id;
    private Long userId;
    private String specialInstructions;
    private Date createdAt;
    private List<OrderItem> orderItems;
}
