package io.github.fraolme.services.basket.integrationevents.handlers;

import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.basket.integrationevents.events.OrderStartedIntegrationEvent;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderStartedIntegrationEventHandler implements IIntegrationEventHandler<OrderStartedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStartedIntegrationEventHandler.class);
    private final RedisBasketRepository redisBasketRepository;

    public OrderStartedIntegrationEventHandler(RedisBasketRepository redisBasketRepository) {
            this.redisBasketRepository = redisBasketRepository;
    }

    @Override
    public void handle(OrderStartedIntegrationEvent event) {
        log.info("--- Handling integration event: {} at Basket Service - {}", event.getId(), event);
        redisBasketRepository.deleteById(event.getUserId());
    }
}
