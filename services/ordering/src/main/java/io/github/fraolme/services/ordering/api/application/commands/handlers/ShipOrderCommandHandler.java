package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.SetStockConfirmedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class ShipOrderCommandHandler {

    private final OrderRepository orderRepository;

    public ShipOrderCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(ShipOrderCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setShippedStatus();
            orderRepository.save(order);
        }
    }

}

