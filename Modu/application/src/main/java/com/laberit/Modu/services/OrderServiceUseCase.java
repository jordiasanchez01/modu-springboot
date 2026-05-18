package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.CartNotFoundException;
import com.laberit.Modu.domain.exceptions.OrderNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.domain.model.response.OrderResult;
import com.laberit.Modu.ports.driven.CartRepositoryPort;
import com.laberit.Modu.ports.driven.OrderRepositoryPort;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.ports.driving.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceUseCase implements OrderServicePort {
    private final OrderRepositoryPort orderRepositoryPort;
    private final CartRepositoryPort cartRepositoryPort;

    @Override
    public Order findOrderById(Long id) {
        return null;
    }

    @Override
    public Order findOrderByUserId(Long userId) {
        return orderRepositoryPort.findByUserId(userId)
                .orElseThrow(()-> new OrderNotFoundException(userId.toString()));
    }

    @Override
    @Transactional
    public Order addOrder(String deviceId, AddOrderCommand command) {
        Order order = new Order();
        if (validateAddOrderCommand(command)) {
            Long userId = Long.valueOf(deviceId);
            order.setUserId(userId);
            System.out.println("This line in addOrder fires");
            Order savedOrder = orderRepositoryPort.saveWithoutItems(order);
            System.out.println("This line after first Save in addOrder fires");
            order = mapOrderCommandToOrder(command, userId, savedOrder);
            savedOrder = orderRepositoryPort.save(order);


            return savedOrder;
        }
        else {
            return null;
        }
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

    private Order mapOrderCommandToOrder(AddOrderCommand command, Long userId, Order order) {

        Cart cart = cartRepositoryPort.findByUserId(userId)
                .orElseThrow(()-> new CartNotFoundException(userId));

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem: cart.getCartItems()){
            OrderItem item = OrderItem.builder()
                    .orderId(order.getId())
                    .productVariantId(cartItem.getProductVariantId())
                    .unitPrice(cartItem.getUnitPrice())
                    .quantity(cartItem.getQuantity())
                    .build();
            System.out.println("OrderItem: "+item.toString());
            orderItems.add(item);
        }

        order.setUserId(cart.getUserId());
        order.setSpecialInstructions(command.specialInstructions());
        order.setOrderItems(orderItems);

        return order;
    }

    private Boolean validateAddOrderCommand(AddOrderCommand command){
        if (command.isPaid()==null){
            throw new RuntimeException("null value found in AddOrderCommand.isPaid");
        }
        if (command.specialInstructions()==null){
            throw new RuntimeException("null value found in AddOrderCommand.specialInstructions");
        }
        return command.isPaid();
    }
}
