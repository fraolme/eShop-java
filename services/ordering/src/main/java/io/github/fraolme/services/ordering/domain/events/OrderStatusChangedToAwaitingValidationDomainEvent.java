package io.github.fraolme.services.ordering.domain.events;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderItem;
import java.util.List;

public record OrderStatusChangedToAwaitingValidationDomainEvent(Long orderId, List<OrderItem> orderItems)
    implements Notification {}
