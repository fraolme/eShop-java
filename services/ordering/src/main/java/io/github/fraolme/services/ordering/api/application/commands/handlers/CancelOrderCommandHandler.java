package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class CancelOrderCommandHandler implements Command.Handler<CancelOrderCommand, Voidy> {

    private final OrderRepository orderRepository;

    public CancelOrderCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Voidy handle(CancelOrderCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setCancelledStatus();
            orderRepository.save(order);
        }

        return new Voidy();
    }
}
