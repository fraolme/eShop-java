package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.commands.SetPaidOrderStatusCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class SetPaidOrderStatusCommandHandler {

    private final OrderRepository orderRepository;

    public SetPaidOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(SetPaidOrderStatusCommand cmd) {
        //TODO: simulate a work time for validating the payment
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setPaidStatus();
            orderRepository.save(order);
        }
    }

}
