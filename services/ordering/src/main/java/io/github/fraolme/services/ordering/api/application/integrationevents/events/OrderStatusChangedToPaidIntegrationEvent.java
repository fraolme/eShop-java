package io.github.fraolme.services.ordering.api.application.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import io.github.fraolme.services.ordering.api.application.models.OrderStockItem;
import java.util.List;

public class OrderStatusChangedToPaidIntegrationEvent extends IntegrationEvent {

    private final Long orderId;
    private final String orderStatus;
    private final String buyerName;
    private final List<OrderStockItem> orderStockItems;

    public OrderStatusChangedToPaidIntegrationEvent(Long orderId, String orderStatus, String buyerName,
                                                                  List<OrderStockItem> orderStockItems) {
        this.orderId = orderId;
        this.orderStockItems = orderStockItems;
        this.orderStatus = orderStatus;
        this.buyerName = buyerName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public List<OrderStockItem> getOrderStockItems() {
        return orderStockItems;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
