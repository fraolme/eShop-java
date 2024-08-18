package io.github.fraolme.services.basket.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

public class OrderStartedIntegrationEvent extends IntegrationEvent {

    private String userId;

    public OrderStartedIntegrationEvent() {}

    public OrderStartedIntegrationEvent(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
