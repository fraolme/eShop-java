package io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderStockConfirmed;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderStatus;
import io.github.fraolme.services.ordering.domain.events.OrderStatusChangedToStockConfirmedDomainEvent;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderStatusChangedToStockConfirmedDomainEventHandler
        implements Notification.Handler<OrderStatusChangedToStockConfirmedDomainEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusChangedToStockConfirmedDomainEventHandler.class);
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;

    public OrderStatusChangedToStockConfirmedDomainEventHandler (OrderRepository orderRepository,
                                           BuyerRepository buyerRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
    }

    @Override
    public void handle(OrderStatusChangedToStockConfirmedDomainEvent event) {
        log.trace("Order with Id: {} has been successfully updated to status {} ({})",
                event.orderId(),
                "Stock Confirmed",
                OrderStatus.stockConfirmed.getId());

        var order = orderRepository.findById(event.orderId()).get();
        var buyer = buyerRepository.findById(order.getBuyerId()).get();

        //TODO: send OrderStatusChangedToStockConfirmedIntegrationEvent
    }
}
