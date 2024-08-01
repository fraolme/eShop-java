package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.SetStockConfirmedOrderStatusCommand;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class SetStockConfirmedOrderStatusCommandHandler implements Command.Handler<SetStockConfirmedOrderStatusCommand, Voidy> {

    private final OrderRepository orderRepository;

    public SetStockConfirmedOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Voidy handle(SetStockConfirmedOrderStatusCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setStockConfirmedStatus();
            orderRepository.save(order);
        }

        return new Voidy();
    }

}
