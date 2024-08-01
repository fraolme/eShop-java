package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderDraftCommand;
import io.github.fraolme.services.ordering.api.application.models.BasketItem;
import io.github.fraolme.services.ordering.api.application.models.OrderDraftDTO;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderDraftCommandHandler implements Command.Handler<CreateOrderDraftCommand, OrderDraftDTO> {

    @Override
    public OrderDraftDTO handle(CreateOrderDraftCommand cmd) {
        var order = Order.NewDraft();
        var orderItems = cmd.items().stream().map(BasketItem::toOrderItemDTO);
        orderItems.forEach(item -> {
            try {
                order.addOrderItem(item.productId(), item.productName(), item.unitPrice(),
                        item.discount(), item.pictureUrl(), item.units());
            } catch (OrderingDomainException e) {
                throw new RuntimeException(e);
            }
        });

        return OrderDraftDTO.fromOrder(order);
    }
}
