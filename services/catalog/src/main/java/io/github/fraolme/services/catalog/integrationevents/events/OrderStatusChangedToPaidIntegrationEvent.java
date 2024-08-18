package io.github.fraolme.services.catalog.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

import java.util.List;

public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private Long orderId;
    private List<OrderStockItem> orderStockItems;

    public OrderStatusChangedToPaidIntegrationEvent() {
    }

    public OrderStatusChangedToPaidIntegrationEvent(Long orderId, List<OrderStockItem> orderStockItems) {
        this.orderId = orderId;
        this.orderStockItems = orderStockItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderStockItem> getOrderStockItems() {
        return orderStockItems;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderStockItems(List<OrderStockItem> orderStockItems) {
        this.orderStockItems = orderStockItems;
    }
}

