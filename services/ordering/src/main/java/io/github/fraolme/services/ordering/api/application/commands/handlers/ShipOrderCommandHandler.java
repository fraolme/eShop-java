package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class ShipOrderCommandHandler implements Command.Handler<ShipOrderCommand, Voidy> {

    private final OrderRepository orderRepository;

    public ShipOrderCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Voidy handle(ShipOrderCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setShippedStatus();
            orderRepository.save(order);
        }

        return new Voidy();
    }

}

