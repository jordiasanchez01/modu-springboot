package com.laberit.Modu.services;

import com.laberit.Modu.domain.exceptions.OrderNotFoundException;
import com.laberit.Modu.domain.model.*;
import com.laberit.Modu.domain.model.response.CheckoutResult;
import com.laberit.Modu.domain.model.response.GetCartResponse;
import com.laberit.Modu.ports.driven.OrderRepositoryPort;
import com.laberit.Modu.ports.driven.ProductVariantRepositoryPort;
import com.laberit.Modu.ports.driving.CartServicePort;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.ports.driving.command.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
    private final OrderRepositoryPort orderRepositoryPort;
    private final CartServicePort cartServicePort;
    private final ProductVariantRepositoryPort productVariantRepositoryPort;

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
    public CheckoutResult addOrder(String deviceId, AddOrderCommand command) {
        Order order = new Order();
        if (validateAddOrderCommand(command)) {
            Long userId = Long.valueOf(deviceId);
            GetCartResponse cartResponse = cartServicePort.findCartByUserId(userId);

            if (cartResponse.changedPrices().isEmpty()) {
                order.setUserId(userId);
                //System.out.println("This line in addOrder fires");
                Order savedOrder = orderRepositoryPort.saveWithoutItems(order);
                //System.out.println("This line after first Save in addOrder fires");
                order = mapOrderCommandToOrder(command, cartResponse.cart(), savedOrder);
                savedOrder = orderRepositoryPort.save(order);
                updateProductVariantStock(savedOrder);
                return new CheckoutResult(savedOrder, cartResponse);
            }

            return new CheckoutResult(order, cartResponse);
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

    private Order mapOrderCommandToOrder(AddOrderCommand command, Cart cart, Order order) {

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

    private List<ProductVariant> updateProductVariantStock(Order order) {

        Set<Long> variantIds = order.getOrderItems().stream()
                .map(OrderItem::getProductVariantId)
                .collect(Collectors.toSet());

        Map<Long, Integer> quantityMap = order.getOrderItems().stream()
                .collect(Collectors.toMap(
                        OrderItem::getProductVariantId,
                        OrderItem::getQuantity
                ));

        List<ProductVariant> variants = productVariantRepositoryPort.findAllByIdInSet(variantIds)
                .stream()
                .toList();

        variants.forEach(variant ->
                variant.setStock(variant.getStock() - quantityMap.get(variant.getId()))
        );

        return productVariantRepositoryPort.saveAll(variants);
    }
}
