package io.github.fraolme.services.ordering.integrationevents;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.UserCheckoutAcceptedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.UserCheckoutAcceptedIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.models.CustomerBasket;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserCheckoutAcceptedIntegrationEventHandlerTests {

    @Mock
    Pipeline pipeline;

    @InjectMocks
    UserCheckoutAcceptedIntegrationEventHandler handler;

    @Test
    void sendsCreateOrderCommandWhenRequestIdExists() {
        // arrange
        var buyerId = UUID.randomUUID();
        var requestId = UUID.randomUUID();
        var basket = new CustomerBasket(buyerId.toString(), List.of());
        var event = new UserCheckoutAcceptedIntegrationEvent(buyerId, "username", "city", "street",
                "state", "country", "zipCode", "cardNumber", "cardHolder", ZonedDateTime.now().plusYears(1), "cardSecurity",
                1L, "buyer", requestId, basket);
        // act
        handler.handle(event);
        // assert
        verify(pipeline).send(any(IdentifiedCommand.class));
    }

    @Test
    void noCommandIsSentWithoutRequestId() {
        var buyerId = UUID.randomUUID();
        var basket = new CustomerBasket(buyerId.toString(), List.of());
        var event = new UserCheckoutAcceptedIntegrationEvent(buyerId, "username", "city", "street",
                "state", "country", "zipCode", "cardNumber", "cardHolder", ZonedDateTime.now().plusYears(1), "cardSecurity",
                1L, "buyer", null, basket);
        // act
        handler.handle(event);
        // assert
        verify(pipeline, never()).send(any(IdentifiedCommand.class));
    }
}