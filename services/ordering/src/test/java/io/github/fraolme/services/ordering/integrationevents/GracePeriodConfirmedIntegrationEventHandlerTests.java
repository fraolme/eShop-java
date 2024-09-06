package io.github.fraolme.services.ordering.integrationevents;

import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.GracePeriodConfirmedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.GracePeriodConfirmedIntegrationEventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import an.awesome.pipelinr.Pipeline;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class GracePeriodConfirmedIntegrationEventHandlerTests {

    @Mock
    Pipeline pipeline;

    @InjectMocks
    GracePeriodConfirmedIntegrationEventHandler handler;

    @Test
    void sendsStatusAwaitingValidationCommand() {
        // arrange
        var orderId = 2L;
        var event = new GracePeriodConfirmedIntegrationEvent(orderId);
        // act
        handler.handle(event);
        // assert
        verify(pipeline).send(any(SetAwaitingValidationOrderStatusCommand.class));
    }
}
