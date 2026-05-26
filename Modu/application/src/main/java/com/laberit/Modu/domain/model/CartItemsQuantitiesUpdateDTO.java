package com.laberit.Modu.domain.model;

import com.laberit.Modu.ports.driving.command.UpdateCartItemQuantityCommand;
import lombok.Builder;

import java.util.List;


@Builder
public record CartItemsQuantitiesUpdateDTO(
        String deviceId,
        List<UpdateCartItemQuantityCommand> cartItemCommands
    ){}

