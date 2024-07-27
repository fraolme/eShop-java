package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.SetStockRejectedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class SetStockRejectedOrderStatusCommandHandler {

    private final OrderRepository orderRepository;

    public SetStockRejectedOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(SetStockRejectedOrderStatusCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setCancelledStatusWhenStockIsRejected(cmd.orderStockItems());
            orderRepository.save(order);
        }
    }

}
