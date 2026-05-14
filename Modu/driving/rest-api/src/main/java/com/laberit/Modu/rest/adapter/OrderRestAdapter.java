package com.laberit.Modu.rest.adapter;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.ports.driving.OrderServicePort;
import com.laberit.Modu.rest.generated.api.CheckoutApi;
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
    public ResponseEntity<OrderResponse> checkoutCart(String xDeviceId) {
        return null;
    }

    @Override
    public ResponseEntity<OrderResponse> getOrder(String xDeviceId) {

        Order order = orderServicePort.findOrderByUserId(Long.valueOf(xDeviceId));

        OrderResponse orderResponse = orderMapper.toOrderResponse(order);

        return ResponseEntity.ok(orderResponse);
    }

}
