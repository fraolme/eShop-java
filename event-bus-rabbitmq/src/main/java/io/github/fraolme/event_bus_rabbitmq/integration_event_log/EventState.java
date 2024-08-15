package io.github.fraolme.event_bus_rabbitmq.integration_event_log;

public enum EventState {
    NotPublished,
    InProgress,
    Published,
    PublishedFailed
}
