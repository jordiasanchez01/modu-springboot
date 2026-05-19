package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.response.CheckoutResult;
import com.laberit.Modu.domain.model.response.GetCartResponse;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import com.laberit.Modu.rest.generated.api.CheckoutApi;
import com.laberit.Modu.rest.generated.model.*;
import com.laberit.Modu.rest.mapper.CartRestMapper;
import com.laberit.Modu.rest.mapper.OrderRestMapper;
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


    @Override
    public ResponseEntity<CheckoutResponse> checkoutCart(String xDeviceId, AddOrderRequest addOrderRequest) {
        AddOrderCommand command = orderMapper.toAddOrderCommand(addOrderRequest);
        CheckoutResult result = orderServicePort.addOrder(xDeviceId, command);

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
                            true,
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
    public ResponseEntity<OrderResponse> getOrder(String xDeviceId) {

        Order order = orderServicePort.findOrderByUserId(Long.valueOf(xDeviceId));

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        return ResponseEntity.ok(orderResponse);
    }

}
