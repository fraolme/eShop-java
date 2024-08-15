package io.github.fraolme.services.basket;

import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.services.basket.integration_events.events.ProductPriceChangedIntegrationEvent;
import io.github.fraolme.services.basket.integration_events.handlers.ProductPriceChangedIntegrationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

// we don't want spring boot to set up database connection which it will try due
// to the repository and entity in the event-bus-rabbitmq library
@SpringBootApplication(
		exclude = {DataSourceAutoConfiguration.class},
		scanBasePackages = {"io.github.fraolme"})
public class BasketApplication {

	private final Logger log = LoggerFactory.getLogger(BasketApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BasketApplication.class, args);
	}

	@Bean
	public CommandLineRunner integrationEventSubscribe(EventBusRabbitMQ eventBus) {
		return args -> {
			eventBus.subscribe(ProductPriceChangedIntegrationEvent.class, ProductPriceChangedIntegrationEventHandler.class);
			log.info("--- Basket service: subscribed to integration events");
		};
	}
}
