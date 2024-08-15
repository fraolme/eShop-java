package io.github.fraolme.event_bus_rabbitmq.events;

public interface IIntegrationEventHandler<T extends IntegrationEvent> {
    void handle(T event);
}
