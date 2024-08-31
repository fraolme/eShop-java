package io.github.fraolme.services.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogRepository;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogService;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.handlers.OrderStatusChangedToAwaitingValidationIntegrationEventHandler;
import io.github.fraolme.services.catalog.integrationevents.handlers.OrderStatusChangedToPaidIntegrationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"io.github.fraolme"})
@EnableJpaRepositories(basePackages = {"io.github.fraolme"})
@EntityScan(basePackages = {"io.github.fraolme"})
public class CatalogApplication {

	private final Logger log = LoggerFactory.getLogger(CatalogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Bean
	IntegrationEventLogService integrationEventLogService(IntegrationEventLogRepository repo, ObjectMapper objectMapper) {
		return new IntegrationEventLogService(repo, objectMapper);
	}

	@Bean
	public CommandLineRunner integrationEventSubscribe(EventBusRabbitMQ eventBus) {
		return args -> {
			eventBus.subscribe(OrderStatusChangedToAwaitingValidationIntegrationEvent.class, OrderStatusChangedToAwaitingValidationIntegrationEventHandler.class);
			eventBus.subscribe(OrderStatusChangedToPaidIntegrationEvent.class, OrderStatusChangedToPaidIntegrationEventHandler.class);
			log.info("--- Catalog service: subscribed to integration events");
		};
	}
}
