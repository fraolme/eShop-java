package io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderShipped;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderGracePeriodConfirmed.OrderStatusChangedToAwaitingValidationDomainEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStatusChangedToShippedIntegrationEvent;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderStatus;
import io.github.fraolme.services.ordering.domain.events.OrderShippedDomainEvent;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderShippedDomainEventHandler implements Notification.Handler<OrderShippedDomainEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusChangedToAwaitingValidationDomainEventHandler.class);
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    public OrderShippedDomainEventHandler (OrderRepository orderRepository, BuyerRepository buyerRepository,
                                           OrderingIntegrationEventService orderingIntegrationEventService) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.orderingIntegrationEventService = orderingIntegrationEventService;
    }

    @Override
    public void handle(OrderShippedDomainEvent event) {
        log.trace("Order with Id: {} has been successfully updated to status {} ({})",
                event.order().getId(),
                "Shipped",
                OrderStatus.shipped.getId());

        var order = orderRepository.findById(event.order().getId()).get();
        var buyer = buyerRepository.findById(order.getBuyerId()).get();

        var integrationEvent = new OrderStatusChangedToShippedIntegrationEvent(order.getId(), order.getOrderStatus().getName(),
                buyer.getName());
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent);
    }
}
