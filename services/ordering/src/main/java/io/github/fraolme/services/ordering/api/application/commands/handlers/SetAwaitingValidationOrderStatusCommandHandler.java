package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class SetAwaitingValidationOrderStatusCommandHandler {

    private final OrderRepository orderRepository;

    public SetAwaitingValidationOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(SetAwaitingValidationOrderStatusCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setAwaitingValidationStatus();
            orderRepository.save(order);
        }
    }

}
