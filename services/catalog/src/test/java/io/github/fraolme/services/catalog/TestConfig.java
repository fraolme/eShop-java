package io.github.fraolme.services.catalog;

import io.github.fraolme.event_bus_rabbitmq.integration_event_log.IntegrationEventLogService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@Configuration
@Profile("test") // need this to activate the TestConfig beans only during testing)
public class TestConfig {

    @Bean
    public IntegrationEventLogService integrationEventLogService() {
        return mock(IntegrationEventLogService.class);
    }

    @Bean
    public CommandLineRunner integrationEventSubscribe() {
        return args -> {}; // No-op for tests
    }
}

