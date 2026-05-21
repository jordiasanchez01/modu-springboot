package com.laberit.Modu.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public record CartDTO (
        String deviceId,
        List<CartItem> cartItems
    ){}

