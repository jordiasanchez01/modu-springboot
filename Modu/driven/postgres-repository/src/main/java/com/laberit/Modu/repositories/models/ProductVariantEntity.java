package com.laberit.Modu.repositories.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_variant")
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column
    private String size;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false)
    private Long productId;
}
