package com.laberit.Modu.ports.driving;

import com.laberit.Modu.domain.model.Order;
import com.laberit.Modu.ports.driving.command.AddOrderItemCommand;
import com.laberit.Modu.ports.driving.command.UpdateOrderCommand;

public interface OrderServicePort {

    Order findOrderByUserId(Long userId);

    Order addOrderItemToOrder(AddOrderItemCommand command);

    Order updateOrder(UpdateOrderCommand command);

    void deleteOrder(Long OrderId);

}
