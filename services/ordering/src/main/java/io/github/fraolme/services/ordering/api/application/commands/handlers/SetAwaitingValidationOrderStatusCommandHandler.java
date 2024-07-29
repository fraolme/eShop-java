package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class SetAwaitingValidationOrderStatusCommandHandler implements Command.Handler<SetAwaitingValidationOrderStatusCommand, Voidy> {

    private final OrderRepository orderRepository;

    public SetAwaitingValidationOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Voidy handle(SetAwaitingValidationOrderStatusCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setAwaitingValidationStatus();
            orderRepository.save(order);
        }

        return new Voidy();
    }

}
