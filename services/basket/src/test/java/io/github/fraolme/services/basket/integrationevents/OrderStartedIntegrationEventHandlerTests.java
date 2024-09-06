package io.github.fraolme.services.basket.integrationevents;

import io.github.fraolme.services.basket.integrationevents.events.OrderStartedIntegrationEvent;
import io.github.fraolme.services.basket.integrationevents.handlers.OrderStartedIntegrationEventHandler;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderStartedIntegrationEventHandlerTests {

    @Mock
    RedisBasketRepository redisBasketRepository;

    @InjectMocks
    OrderStartedIntegrationEventHandler handler;

    @Test
    void deletesBasket() {
        // arrange
        var userId = UUID.randomUUID().toString();
        var event = new OrderStartedIntegrationEvent(userId);

        // act
        handler.handle(event);
        // assert
        verify(redisBasketRepository).deleteById(userId);
    }
}
