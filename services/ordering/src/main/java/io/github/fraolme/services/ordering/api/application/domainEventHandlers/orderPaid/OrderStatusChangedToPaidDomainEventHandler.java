package io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderPaid;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderGracePeriodConfirmed.OrderStatusChangedToAwaitingValidationDomainEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.models.OrderStockItem;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderStatus;
import io.github.fraolme.services.ordering.domain.events.OrderStatusChangedToAwaitingValidationDomainEvent;
import io.github.fraolme.services.ordering.domain.events.OrderStatusChangedToPaidDomainEvent;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class OrderStatusChangedToPaidDomainEventHandler
        implements Notification.Handler<OrderStatusChangedToPaidDomainEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusChangedToPaidDomainEvent.class);
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    public OrderStatusChangedToPaidDomainEventHandler(OrderRepository orderRepository, BuyerRepository buyerRepository,
                                                      OrderingIntegrationEventService orderingIntegrationEventService) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.orderingIntegrationEventService = orderingIntegrationEventService;
    }

    @Override
    public void handle(OrderStatusChangedToPaidDomainEvent event) {
        log.trace("Order with Id: {} has been successfully updated to status {} ({})",
                event.orderId(),
                "Paid",
                OrderStatus.paid.getId());

        var order = orderRepository.findById(event.orderId()).get();
        var buyer = buyerRepository.findById(order.getBuyerId()).get();
        var orderStockList = event.orderItems().stream()
                .map(x -> new OrderStockItem(x.getProductId(), x.getUnits()))
                .collect(Collectors.toList());

        var integrationEvent = new OrderStatusChangedToPaidIntegrationEvent(event.orderId(), order.getOrderStatus().getName(),
                buyer.getName(), orderStockList);
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent);
    }
}
