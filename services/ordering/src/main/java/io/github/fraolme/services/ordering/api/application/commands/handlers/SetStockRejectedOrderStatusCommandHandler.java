package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.SetStockRejectedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.domain.repositories.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class SetStockRejectedOrderStatusCommandHandler implements Command.Handler<SetStockRejectedOrderStatusCommand, Voidy> {

    private final OrderRepository orderRepository;

    public SetStockRejectedOrderStatusCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Voidy handle(SetStockRejectedOrderStatusCommand cmd) {
        var orderToUpdate = orderRepository.findById(cmd.orderNumber());
        if(orderToUpdate.isPresent()) {
            var order = orderToUpdate.get();
            order.setCancelledStatusWhenStockIsRejected(cmd.orderStockItems());
            orderRepository.save(order);
        }

        return new Voidy();
    }

}
