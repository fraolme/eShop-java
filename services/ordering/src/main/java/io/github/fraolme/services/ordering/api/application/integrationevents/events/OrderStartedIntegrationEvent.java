package io.github.fraolme.services.ordering.api.application.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import java.util.UUID;

public class OrderStartedIntegrationEvent extends IntegrationEvent {

    private final UUID userId;

    public OrderStartedIntegrationEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
