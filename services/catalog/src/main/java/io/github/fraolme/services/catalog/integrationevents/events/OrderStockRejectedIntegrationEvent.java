package io.github.fraolme.services.catalog.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

import java.util.List;

public class OrderStockRejectedIntegrationEvent extends IntegrationEvent {

    private Long orderId;
    private List<ConfirmedOrderStockItem> orderStockItems;

    public OrderStockRejectedIntegrationEvent() {}

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

