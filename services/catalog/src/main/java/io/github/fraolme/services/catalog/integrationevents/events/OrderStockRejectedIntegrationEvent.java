package io.github.fraolme.services.catalog.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

import java.util.List;

public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {

    private final Long orderId;
    private final List<ConfirmedOrderStockItem> orderStockItems;

    public OrderStockRejectedIntegrationEvent(Long orderId, List<ConfirmedOrderStockItem> orderStockItems) {
        this.orderId = orderId;
        this.orderStockItems = orderStockItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<ConfirmedOrderStockItem> getOrderStockItems() {
        return orderStockItems;
    }
}

