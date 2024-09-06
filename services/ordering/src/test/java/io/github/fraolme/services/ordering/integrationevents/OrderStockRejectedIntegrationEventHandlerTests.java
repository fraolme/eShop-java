package io.github.fraolme.services.ordering.integrationevents;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.SetStockRejectedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderPaymentFailedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStockRejectedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.OrderPaymentFailedIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.handlers.OrderStockRejectedIntegrationEventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderStockRejectedIntegrationEventHandlerTests {

    @Mock
    Pipeline pipeline;

    @InjectMocks
    OrderStockRejectedIntegrationEventHandler handler;

    @Test
    void sendsStockRejectedCommand() {
        // arrange
        var orderId = 2L;
        var event = new OrderStockRejectedIntegrationEvent(orderId, List.of());
        // act
        handler.handle(event);
        // assert
        verify(pipeline).send(any(SetStockRejectedOrderStatusCommand.class));
    }
}
