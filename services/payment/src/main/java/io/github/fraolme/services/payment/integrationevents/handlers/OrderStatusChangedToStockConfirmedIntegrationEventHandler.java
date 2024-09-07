package io.github.fraolme.services.payment.integrationevents.handlers;

import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import io.github.fraolme.services.payment.AppProperties;
import io.github.fraolme.services.payment.integrationevents.events.OrderPaymentFailedIntegrationEvent;
import io.github.fraolme.services.payment.integrationevents.events.OrderPaymentSucceededIntegrationEvent;
import io.github.fraolme.services.payment.integrationevents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusChangedToStockConfirmedIntegrationEventHandler
        implements IIntegrationEventHandler<OrderStatusChangedToStockConfirmedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusChangedToStockConfirmedIntegrationEventHandler.class);
    private final EventBusRabbitMQ eventBus;
    private final AppProperties appProperties;

    public OrderStatusChangedToStockConfirmedIntegrationEventHandler(EventBusRabbitMQ eventBus, AppProperties appProperties) {
        this.eventBus = eventBus;
        this.appProperties = appProperties;
    }

    @Override
    public void handle(OrderStatusChangedToStockConfirmedIntegrationEvent event) {
        log.info("--- Handling integration event: {} at Payment Service - ({})", event.getId(), event);

        //Business feature comment:
        // When OrderStatusChangedToStockConfirmed Integration Event is handled.
        // Here we're simulating that we'd be performing the payment against any payment gateway
        // Instead of a real payment we just take the env. var to simulate the payment.
        // The payment can be successful or it can fail

        IntegrationEvent orderPaymentEvent = null;
        if(this.appProperties.getPaymentSucceeded()) {
            orderPaymentEvent = new OrderPaymentSucceededIntegrationEvent(event.getOrderId());
        } else {
            orderPaymentEvent = new OrderPaymentFailedIntegrationEvent(event.getOrderId());
        }

        log.info("--- Publishing integration event: {} from Payment Service - ({})", orderPaymentEvent.getId(), orderPaymentEvent);
        eventBus.publish(orderPaymentEvent);
    }
}
