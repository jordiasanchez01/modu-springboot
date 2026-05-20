package com.laberit.Modu.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public record CartDTO (
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CartItem> cartItems
    ){}

