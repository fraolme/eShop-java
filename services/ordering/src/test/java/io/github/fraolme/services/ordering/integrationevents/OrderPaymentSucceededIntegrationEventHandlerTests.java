package io.github.fraolme.services.ordering.integrationevents;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.SetPaidOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderPaymentFailedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderPaymentSucceededIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.OrderPaymentFailedIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.OrderPaymentSucceededIntegrationEventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderPaymentSucceededIntegrationEventHandlerTests {

    @Mock
    Pipeline pipeline;

    @InjectMocks
    OrderPaymentSucceededIntegrationEventHandler handler;

    @Test
    void sendsChangeToPaidStatusCommand() {
        // arrange
        var orderId = 2L;
        var event = new OrderPaymentSucceededIntegrationEvent(orderId);
        // act
        handler.handle(event);
        // assert
        verify(pipeline).send(any(SetPaidOrderStatusCommand.class));
    }
}
