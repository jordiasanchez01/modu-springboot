package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.response.CheckoutResult;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import com.laberit.Modu.rest.generated.api.CheckoutApi;
import com.laberit.Modu.rest.generated.model.*;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import com.laberit.Modu.rest.mapper.OrderRestMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@RestController
@RequiredArgsConstructor
public class OrderRestAdapter implements CheckoutApi {
    private final OrderServicePort orderServicePort;
    private final OrderRestMapper orderMapper;
    private final CartRestMapper cartMapper;
    private final HttpServletRequest request;


    @Override
    public ResponseEntity<CheckoutResponse> checkoutCart(AddOrderRequest addOrderRequest) {
        String deviceId = (String) request.getAttribute("deviceId");
        AddOrderCommand command = orderMapper.toAddOrderCommand(addOrderRequest);
        CheckoutResult result = orderServicePort.addOrder(deviceId, command);

        CheckoutResponse response = new CheckoutResponse();
        if (result.cartResponse().changedPrices().isEmpty() && result.cartResponse().insufficientStock().isEmpty()) {
            response.ok(true);
            response.orderId(result.order().getId());
            response.order(
                    orderMapper.toOrderResponse(result.order())
            );
            return ResponseEntity.ok(response);
        } else {

            response.ok(false);
            response.orderId(null);
            response.order(null);

            if (result.cartResponse().cart() != null) {
                CartResponse cartResponse = cartMapper.toCartResponse(result.cartResponse().cart());

                if (!result.cartResponse().changedPrices().isEmpty()) {
                    PriceChangedAlert priceChangedAlert = new PriceChangedAlert(
                            cartMapper.toProductPriceChangeResponseList(result.cartResponse().changedPrices())
                    );
                    cartResponse.setPriceChangedAlert(priceChangedAlert);
                    System.out.println("Checkout failed, prices changed: "+result.cartResponse().changedPrices());
                }
                if (!result.cartResponse().insufficientStock().isEmpty()) {
                    InsufficientStockAlert insufficientStockAlert = new InsufficientStockAlert(
                            cartMapper.toInsufficientStockResponseList(result.cartResponse().insufficientStock())
                    );
                    cartResponse.setInsufficientStockAlert(insufficientStockAlert);
                    System.out.println("Checkout failed, insufficient stocks: "+result.cartResponse().insufficientStock());
                }


                response.cartResponse(cartResponse);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

    }

    @Override
    public ResponseEntity<OrderResponse> getOrder() {
        String deviceId = (String) request.getAttribute("deviceId");

        Order order = orderServicePort.findOrderByDeviceId(deviceId);

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        return ResponseEntity.ok(orderResponse);
    }

}
