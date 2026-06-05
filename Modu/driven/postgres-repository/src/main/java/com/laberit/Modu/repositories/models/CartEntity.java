package com.laberit.Modu.repositories.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name="device_id")
    private String deviceId;
    
    @Column(name="created_at", insertable = false, updatable = false)
    private Instant createdAt;
    @Column(name="updated_at", insertable = false, updatable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "cartId")
    private List<CartItemEntity> cartItems;
}
