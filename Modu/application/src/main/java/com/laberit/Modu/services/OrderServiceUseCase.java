package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.ProductVariantNotFoundException;
import com.laberit.Modu.domain.model.Cart;
import com.laberit.Modu.domain.model.CartItem;
import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.ProductVariant;
import com.laberit.Modu.ports.driven.CartItemRepositoryPort;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driven.OrderRepositoryPort;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.ports.driving.ProductServicePort;
import com.laberit.Modu.ports.driving.ProductVariantServicePort;
import com.laberit.Modu.ports.driving.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceUseCase implements OrderServicePort {
    private final OrderRepositoryPort cartRepositoryPort;


    @Override
    public Order findOrderByUserId(Long userId) {
        return null;
    }

    @Override
    public Order addOrderItemToOrder(AddOrderItemCommand command) {
        return null;
    }

    @Override
    public Order updateOrder(UpdateOrderCommand command) {
        return null;
    }

    @Override
    public void deleteOrder(Long OrderId) {

    }
}
