package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.SetPaidOrderStatusCommand;
import io.github.fraolme.services.ordering.api.domain.repositories.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class SetPaidOrderStatusCommandHandler implements Command.Handler<SetPaidOrderStatusCommand, Voidy> {

    private final OrderRepository orderRepository;

    public SetPaidOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Voidy handle(SetPaidOrderStatusCommand cmd) {
        //TODO: simulate a work time for validating the payment
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setPaidStatus();
            orderRepository.save(order);
        }

        return new Voidy();
    }

}
