package io.github.fraolme.services.payment.integrationevents;

import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.services.payment.AppProperties;
import io.github.fraolme.services.payment.integrationevents.events.OrderPaymentFailedIntegrationEvent;
import io.github.fraolme.services.payment.integrationevents.events.OrderPaymentSucceededIntegrationEvent;
import io.github.fraolme.services.payment.integrationevents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;
import io.github.fraolme.services.payment.integrationevents.handlers.OrderStatusChangedToStockConfirmedIntegrationEventHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderStatusChangedToStockConfirmedIntegrationEventHandlerTests {

    @Mock
    EventBusRabbitMQ eventBus;

    @Mock
    AppProperties appProperties;

    @InjectMocks
    OrderStatusChangedToStockConfirmedIntegrationEventHandler handler;

    @Test
    void orderPaymentSucceededIntegrationEventIsSent() {
        // arrange
        var orderId = 7L;
        var event = new OrderStatusChangedToStockConfirmedIntegrationEvent(orderId, "Stock Confirmed", "buyerName");
        when(appProperties.getPaymentSucceeded()).thenReturn(true);

        // act
        handler.handle(event);

        // assert
        verify(eventBus).publish(any(OrderPaymentSucceededIntegrationEvent.class));
    }

    @Test
    void orderPaymentFailedIntegrationEventIsSent() {
        // arrange
        var orderId = 7L;
        var event = new OrderStatusChangedToStockConfirmedIntegrationEvent(orderId, "Stock Confirmed", "buyerName");
        when(appProperties.getPaymentSucceeded()).thenReturn(false);

        // act
        handler.handle(event);

        // assert
        verify(eventBus).publish(any(OrderPaymentFailedIntegrationEvent.class));
    }
}
