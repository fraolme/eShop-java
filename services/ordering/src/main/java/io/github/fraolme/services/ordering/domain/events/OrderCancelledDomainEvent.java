package io.github.fraolme.services.ordering.domain.events;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;

public record OrderCancelledDomainEvent(Order order) implements Notification {}
