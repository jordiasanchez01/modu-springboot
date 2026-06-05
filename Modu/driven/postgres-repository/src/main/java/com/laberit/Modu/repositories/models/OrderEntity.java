package com.laberit.Modu.repositories.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name="device_id")
    private String deviceId;

    @Column(name="special_instructions")
    private String specialInstructions;
    @Column(name="subtotal_price")
    private double subtotalPrice;
    @Column(name="shipping_costs")
    private double shippingCosts;
    @Column(name="total_price")
    private Double totalPrice;
    @Column(name="created_at", insertable = false, updatable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order")
    private List<OrderItemEntity> orderItems;

}
