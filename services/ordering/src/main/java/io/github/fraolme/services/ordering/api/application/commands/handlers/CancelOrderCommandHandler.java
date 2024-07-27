package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class CancelOrderCommandHandler {

    private final OrderRepository orderRepository;

    public CancelOrderCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(CancelOrderCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setCancelledStatus();
            orderRepository.save(order);
        }
    }
}
