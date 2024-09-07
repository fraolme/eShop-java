package io.github.fraolme.services.payment;

import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.services.payment.integrationevents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;
import io.github.fraolme.services.payment.integrationevents.handlers.OrderStatusChangedToStockConfirmedIntegrationEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

//TODO: don't depend on spring boot starter as it brings many default packages
@SpringBootApplication(
		exclude = {DataSourceAutoConfiguration.class },
		scanBasePackages = {"io.github.fraolme"})
public class PaymentApplication {

	private final Logger log = LoggerFactory.getLogger(PaymentApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PaymentApplication.class, args);
	}

	@Bean
	public CommandLineRunner integrationEventSubscribe(EventBusRabbitMQ eventBus) {
		return args -> {
			eventBus.subscribe(OrderStatusChangedToStockConfirmedIntegrationEvent.class, OrderStatusChangedToStockConfirmedIntegrationEventHandler.class);
			log.info("--- Payment service: subscribed to integration events");
		};
	}
}
