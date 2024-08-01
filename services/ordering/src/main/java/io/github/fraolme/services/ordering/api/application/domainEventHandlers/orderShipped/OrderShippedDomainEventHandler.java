package io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderShipped;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderGracePeriodConfirmed.OrderStatusChangedToAwaitingValidationDomainEventHandler;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderStatus;
import io.github.fraolme.services.ordering.domain.events.OrderShippedDomainEvent;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderShippedDomainEventHandler implements Notification.Handler<OrderShippedDomainEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusChangedToAwaitingValidationDomainEventHandler.class);
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;

    public OrderShippedDomainEventHandler (OrderRepository orderRepository,
                                                                    BuyerRepository buyerRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    public void handle(OrderShippedDomainEvent event) {
        log.trace("Order with Id: {} has been successfully updated to status {} ({})",
                event.order().getId(),
                "Shipped",
                OrderStatus.shipped.getId());

        var order = orderRepository.findById(event.order().getId()).get();
        var buyer = buyerRepository.findById(order.getBuyerId()).get();

        //TODO: send OrderStatusChangedToShippedIntegrationEvent
    }
}
