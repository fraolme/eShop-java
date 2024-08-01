package io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderStarted;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.domain.events.OrderStartedDomainEvent;

public class SendEmailToCustomerWhenOrderStartedDomainEventHandler
        implements Notification.Handler<OrderStartedDomainEvent> {

    public SendEmailToCustomerWhenOrderStartedDomainEventHandler() {
    }

    @Override
    public void handle(OrderStartedDomainEvent event) {
        //TODO: send email
    }
}
