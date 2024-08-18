package io.github.fraolme.services.catalog.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

public class OrderStockConfirmedIntegrationEvent extends IntegrationEvent {

    private final Long orderId;

    public OrderStockConfirmedIntegrationEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}

