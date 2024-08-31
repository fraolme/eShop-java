package io.github.fraolme.services.ordering.api.application.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

public class OrderStatusChangedToSubmittedIntegrationEvent extends IntegrationEvent {

    private Long orderId;
    private String orderStatus;
    private String buyerName;

    public OrderStatusChangedToSubmittedIntegrationEvent(){}

    public OrderStatusChangedToSubmittedIntegrationEvent(Long orderId, String orderStatus, String buyerName) {
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
