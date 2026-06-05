package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.response.CheckoutResult;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import com.laberit.Modu.ports.driving.command.AddOrderItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateOrderCommand;

public interface OrderServicePort {

    Order findOrderById(Long id);

    Order findOrderByDeviceId(String deviceId);

    CheckoutResult addOrder(String deviceId, AddOrderCommand command);

}
