package io.github.fraolme.services.ordering.integrationevents;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.GracePeriodConfirmedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderPaymentFailedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.GracePeriodConfirmedIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.OrderPaymentFailedIntegrationEventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderPaymentFailedIntegrationEventHandlerTests {

    @Mock
    Pipeline pipeline;

    @InjectMocks
    OrderPaymentFailedIntegrationEventHandler handler;

    @Test
    void sendsCancelCommand() {
        // arrange
        var orderId = 2L;
        var event = new OrderPaymentFailedIntegrationEvent(orderId);
        // act
        handler.handle(event);
        // assert
        verify(pipeline).send(any(CancelOrderCommand.class));
    }
}
