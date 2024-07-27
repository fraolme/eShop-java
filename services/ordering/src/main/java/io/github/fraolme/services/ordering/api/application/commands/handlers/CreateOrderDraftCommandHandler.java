package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.CreateOrderDraftCommand;
import io.github.fraolme.services.ordering.api.application.models.BasketItem;
import io.github.fraolme.services.ordering.api.application.models.OrderDraftDTO;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.Order;
import org.axonframework.commandhandling.CommandHandler;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderDraftCommandHandler {

    @CommandHandler
    public OrderDraftDTO handle(CreateOrderDraftCommand cmd) {
        var order = Order.NewDraft();
        var orderItems = cmd.items().stream().map(BasketItem::toOrderItemDTO);
        orderItems.forEach(item -> order.addOrderItem(item.productId(), item.productName(), item.unitPrice(),
                item.discount(), item.pictureUrl(), item.units()));

        return OrderDraftDTO.fromOrder(order);
    }
}
