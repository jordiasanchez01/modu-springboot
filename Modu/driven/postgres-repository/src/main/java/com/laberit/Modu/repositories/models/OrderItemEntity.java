package com.laberit.Modu.repositories.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariantEntity productVariant;

    @Column(name="unit_price")
    private Double unitPrice;

    @Column(name="total_price")
    private Double totalPrice;

    @Column(nullable = false)
    private Integer quantity;
}
