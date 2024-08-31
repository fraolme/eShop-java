package io.github.fraolme.services.ordering.api.application.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import java.util.UUID;

public class OrderStartedIntegrationEvent extends IntegrationEvent {

    private UUID userId;

    public OrderStartedIntegrationEvent() {}

    public OrderStartedIntegrationEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
