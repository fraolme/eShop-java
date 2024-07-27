package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.commands.SetStockConfirmedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class SetStockConfirmedOrderStatusCommandHandler {

    private final OrderRepository orderRepository;

    public SetStockConfirmedOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(SetStockConfirmedOrderStatusCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setStockConfirmedStatus();
            orderRepository.save(order);
        }
    }

}
