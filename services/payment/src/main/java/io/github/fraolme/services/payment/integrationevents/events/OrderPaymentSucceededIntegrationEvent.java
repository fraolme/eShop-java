package io.github.fraolme.services.payment.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

public class OrderPaymentSucceededIntegrationEvent extends IntegrationEvent {

    private Long orderId;

    public OrderPaymentSucceededIntegrationEvent() {}

    public OrderPaymentSucceededIntegrationEvent(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
