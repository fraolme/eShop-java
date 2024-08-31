package io.github.fraolme.services.ordering.infrastructure.eventdispatcher;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class IntegrationEventDispatcher {

    private final Logger log = LoggerFactory.getLogger(IntegrationEventDispatcher.class);

    private final OrderingIntegrationEventService orderingIntegrationEventService;

    public IntegrationEventDispatcher(OrderingIntegrationEventService orderingIntegrationEventService) {
        this.orderingIntegrationEventService = orderingIntegrationEventService;
    }

    // will publish to the event bus after the transaction started with TransactionMiddleware ends
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void dispatchIntegrationEvent(IntegrationEvent event) {
        log.info("--- Dispatching Integration Event {} for publishing", event.getClass().getSimpleName());
        this.orderingIntegrationEventService.publishEventThroughEventBus(event.getId());
    }
}
