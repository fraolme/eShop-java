package io.github.fraolme.services.catalog.integrationevents;

import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CatalogIntegrationEventService {

    private final Logger log = LoggerFactory.getLogger(CatalogIntegrationEventService.class);
    private final IntegrationEventLogService eventLogService;
    private final EventBusRabbitMQ eventBus;

    public CatalogIntegrationEventService(IntegrationEventLogService eventLogService, EventBusRabbitMQ eventBus) {
        this.eventLogService = eventLogService;
        this.eventBus = eventBus;
    }

    public void publishThroughEventBus(IntegrationEvent event) {
        try {
            log.info("--- Publishing integration event:{} from Catalog - {}", event.getId(), event);
            eventLogService.markEventAsInProgress(event.getId());
            eventBus.publish(event);
            eventLogService.markEventAsPublished(event.getId());
        } catch (Exception ex) {
            log.error("ERROR Publishing integration event: {} from Catalog - {}", event.getId(), event, ex);
            eventLogService.markEventAsFailed(event.getId());
        }
    }

    public void saveEvent(IntegrationEvent event) {
        eventLogService.saveEvent(event, UUID.randomUUID());
    }
}
