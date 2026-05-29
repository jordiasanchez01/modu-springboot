package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.response.CheckoutResult;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.rest.generated.api.CheckoutApi;
import com.laberit.Modu.rest.generated.model.*;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import com.laberit.Modu.rest.mapper.OrderRestMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderRestAdapter implements CheckoutApi {
    private final OrderServicePort orderServicePort;
    private final OrderRestMapper orderMapper;
    private final CartRestMapper cartMapper;


    @Override
    public ResponseEntity<CheckoutResponse> checkoutCart(AddOrderRequest addOrderRequest) {
        String deviceId = currentDeviceId();
        CheckoutResult result = orderServicePort.addOrder(deviceId, orderMapper.toAddOrderCommand(addOrderRequest));

        CheckoutResponse response = new CheckoutResponse().orderPlaced(result.isOrderPlaced());

        if (result.isOrderPlaced()) {
            response.orderId(result.order().getId());
            response.order(orderMapper.toOrderResponse(result.order()));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.cartResponse(cartMapper.toValidatedCartResponse(result.cartResponse()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(Long orderId) {

        Order order = orderServicePort.findOrderById(orderId);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        return ResponseEntity.ok(orderResponse);
    }

    private String currentDeviceId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}