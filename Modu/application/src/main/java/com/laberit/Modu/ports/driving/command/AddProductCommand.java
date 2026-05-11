package com.laberit.Modu.ports.driving.command;

public record AddProductCommand(
        String name,
        String description,
        String imageUrl,
        Double price,
        Boolean active,
        Long productId
) {
}
