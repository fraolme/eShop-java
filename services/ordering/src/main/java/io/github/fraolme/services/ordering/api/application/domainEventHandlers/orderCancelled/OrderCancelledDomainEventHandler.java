package io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderCancelled;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStatusChangedToCancelledIntegrationEvent;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderStatus;
import io.github.fraolme.services.ordering.domain.events.OrderCancelledDomainEvent;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderCancelledDomainEventHandler implements Notification.Handler<OrderCancelledDomainEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderCancelledDomainEventHandler.class);
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    public OrderCancelledDomainEventHandler(OrderRepository orderRepository, BuyerRepository buyerRepository,
                                            OrderingIntegrationEventService orderingIntegrationEventService) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.orderingIntegrationEventService = orderingIntegrationEventService;
    }

    @Override
    public void handle(OrderCancelledDomainEvent event) {
       log.trace("Order with Id: {} has been successfully updated to status {} ({})",
               event.order().getId(),
               "Cancelled",
               OrderStatus.cancelled);

       var order = orderRepository.findById(event.order().getId()).get();
       var buyer = buyerRepository.findById(order.getBuyerId()).get();

        var integrationEvent = new OrderStatusChangedToCancelledIntegrationEvent(order.getId(), order.getOrderStatus().getName(),
                buyer.getName());
        this.orderingIntegrationEventService.addAndSaveEvent(integrationEvent);
    }
}
