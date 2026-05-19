package com.laberit.Modu.ports.driving.command;

public record UpdateProductVariantCommand(
        Long id,
        String name,
        String size,
        String color,
        Integer stock,
        Boolean active,
        Long productId
) {
}
