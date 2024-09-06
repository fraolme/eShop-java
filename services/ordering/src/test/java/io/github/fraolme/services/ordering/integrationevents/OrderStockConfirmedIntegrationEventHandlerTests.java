package io.github.fraolme.services.ordering.integrationevents;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.SetStockConfirmedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderPaymentFailedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStockConfirmedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.OrderPaymentFailedIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.OrderStockConfirmedIntegrationEventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderStockConfirmedIntegrationEventHandlerTests {

    @Mock
    Pipeline pipeline;

    @InjectMocks
    OrderStockConfirmedIntegrationEventHandler handler;

    @Test
    void sendsStockConfirmedCommand() {
        // arrange
        var orderId = 2L;
        var event = new OrderStockConfirmedIntegrationEvent(orderId);
        // act
        handler.handle(event);
        // assert
        verify(pipeline).send(any(SetStockConfirmedOrderStatusCommand.class));
    }
}
