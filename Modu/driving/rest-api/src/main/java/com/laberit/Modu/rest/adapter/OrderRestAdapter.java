package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import com.laberit.Modu.rest.generated.api.CheckoutApi;
import com.laberit.Modu.rest.generated.model.AddOrderRequest;
import com.laberit.Modu.rest.generated.model.CheckoutResponse;
import com.laberit.Modu.rest.generated.model.OrderResponse;
import com.laberit.Modu.rest.mapper.OrderRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class OrderRestAdapter implements CheckoutApi {
    private final OrderServicePort orderServicePort;
    private final OrderRestMapper orderMapper;


    @Override
    public ResponseEntity<CheckoutResponse> checkoutCart(String xDeviceId, AddOrderRequest addOrderRequest) {
        AddOrderCommand command = orderMapper.toAddOrderCommand(addOrderRequest);
        Order order = orderServicePort.addOrder(xDeviceId, command);
        CheckoutResponse response = new CheckoutResponse();
        if (order != null){
            response.ok(true);
            response.orderId(order.getId());
            response.order(
                    orderMapper.toOrderResponse(order)
            );
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(String xDeviceId) {

        Order order = orderServicePort.findOrderByUserId(Long.valueOf(xDeviceId));

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        return ResponseEntity.ok(orderResponse);
    }

}
