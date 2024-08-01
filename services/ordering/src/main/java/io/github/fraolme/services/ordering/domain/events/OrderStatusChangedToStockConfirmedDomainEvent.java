package io.github.fraolme.services.ordering.domain.events;

import an.awesome.pipelinr.Notification;

public record OrderStatusChangedToStockConfirmedDomainEvent(Long orderId) implements Notification {
}
