package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.domain.model.response.OrderResult;
import com.laberit.Modu.ports.driving.command.AddOrderCommand;
import com.laberit.Modu.ports.driving.command.AddOrderItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateOrderCommand;

public interface OrderServicePort {

    Order findOrderById(Long id);

    Order findOrderByUserId(Long userId);

    Order addOrder(String deviceId, AddOrderCommand command);

    Order addOrderItemToOrder(AddOrderItemCommand command);

    Order updateOrder(UpdateOrderCommand command);

    void deleteOrder(Long OrderId);

}
