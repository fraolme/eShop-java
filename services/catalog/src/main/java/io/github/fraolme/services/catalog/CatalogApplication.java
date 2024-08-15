package io.github.fraolme.services.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogRepository;
import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"io.github.fraolme"})
@EnableJpaRepositories(basePackages = {"io.github.fraolme"})
@EntityScan(basePackages = {"io.github.fraolme"})
public class CatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogApplication.class, args);
	}

	@Bean
	IntegrationEventLogService integrationEventLogService(IntegrationEventLogRepository repo, ObjectMapper objectMapper) {
		return new IntegrationEventLogService(repo, objectMapper);
	}
}
