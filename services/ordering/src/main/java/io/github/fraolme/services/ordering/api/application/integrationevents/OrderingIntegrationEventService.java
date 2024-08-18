package io.github.fraolme.services.ordering.api.application.integrationevents;

import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderingIntegrationEventService {

    private static final Logger log = LoggerFactory.getLogger(OrderingIntegrationEventService.class);
    private final EventBusRabbitMQ eventBus;
    private final IntegrationEventLogService eventLogService;

    public OrderingIntegrationEventService(EventBusRabbitMQ eventBus, IntegrationEventLogService eventLogService) {
        this.eventBus = eventBus;
        this.eventLogService = eventLogService;
    }

    public void publishEventsThroughEventBus(UUID transactionId) {
        var pendingEvents = eventLogService.retrieveEventLogsPendingToPublish(transactionId);
        for(var logEvt : pendingEvents) {
            var integrationEvent = logEvt.parseContent();
            log.info("--- Publishing integration event: {} from Ordering service - ({})",
                    logEvt.getEventId(), integrationEvent);
            try {
                eventLogService.markEventAsInProgress(logEvt.getEventId());
                eventBus.publish(integrationEvent);
                eventLogService.markEventAsPublished(logEvt.getEventId());
            } catch (Exception ex) {
                log.error("ERROR publishing integration event: {} from Ordering service", logEvt.getEventId(), ex);
                eventLogService.markEventAsFailed(logEvt.getEventId());
            }
        }
    }

    public void addAndSaveEvent(IntegrationEvent evt) {
        log.info("--- Enqueuing integration event {} to repository ({})", evt.getId(), evt);
        //TODO: get a transaction specific uuid
        eventLogService.saveEvent(evt, UUID.randomUUID());
    }
}
