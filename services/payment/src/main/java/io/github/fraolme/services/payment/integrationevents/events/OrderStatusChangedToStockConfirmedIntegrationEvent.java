package io.github.fraolme.services.payment.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

public class OrderStatusChangedToStockConfirmedIntegrationEvent extends IntegrationEvent {
    private Long orderId;
    private String orderStatus;
    private String buyerName;

    public OrderStatusChangedToStockConfirmedIntegrationEvent(){}

    public OrderStatusChangedToStockConfirmedIntegrationEvent(Long orderId, String orderStatus, String buyerName) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.buyerName = buyerName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
