package io.github.fraolme.services.ordering.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogRepository;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.*;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IntegrationEventConfiguration {

    private final Logger log = LoggerFactory.getLogger(IntegrationEventConfiguration.class);

    @Bean
    IntegrationEventLogService integrationEventLogService(IntegrationEventLogRepository repo, ObjectMapper objectMapper) {
        return new IntegrationEventLogService(repo, objectMapper);
    }

    @Bean
    public CommandLineRunner integrationEventSubscribe(EventBusRabbitMQ eventBus) {
        return args -> {
            eventBus.subscribe(GracePeriodConfirmedIntegrationEvent.class, GracePeriodConfirmedIntegrationEventHandler.class);
            eventBus.subscribe(OrderPaymentFailedIntegrationEvent.class, OrderPaymentFailedIntegrationEventHandler.class);
            eventBus.subscribe(OrderPaymentSucceededIntegrationEvent.class, OrderPaymentSucceededIntegrationEventHandler.class);
            eventBus.subscribe(OrderStockConfirmedIntegrationEvent.class, OrderStockConfirmedIntegrationEventHandler.class);
            eventBus.subscribe(OrderStockRejectedIntegrationEvent.class, OrderStockRejectedIntegrationEventHandler.class);
            eventBus.subscribe(UserCheckoutAcceptedIntegrationEvent.class, UserCheckoutAcceptedIntegrationEventHandler.class);
            log.info("--- Ordering service: subscribed to integration events");
        };
    }
}
