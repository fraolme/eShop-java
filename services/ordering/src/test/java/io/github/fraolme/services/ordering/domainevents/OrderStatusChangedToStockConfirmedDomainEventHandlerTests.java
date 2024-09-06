package io.github.fraolme.services.ordering.domainevents;

import io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderStockConfirmed.OrderStatusChangedToStockConfirmedDomainEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.Buyer;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderStatus;
import io.github.fraolme.services.ordering.domain.events.OrderStatusChangedToStockConfirmedDomainEvent;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderStatusChangedToStockConfirmedDomainEventHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @Mock
    BuyerRepository buyerRepository;

    @Mock
    OrderingIntegrationEventService orderingIntegrationEventService;

    @InjectMocks
    OrderStatusChangedToStockConfirmedDomainEventHandler handler;

    OrderStatusChangedToStockConfirmedDomainEvent domainEvent;
    Long orderId = 2L;
    Long buyerId = 3L;

    @BeforeEach
    void setUp() {
        this.domainEvent = new OrderStatusChangedToStockConfirmedDomainEvent(orderId);
    }

    @Test
    void successfullySendsIntegrationEvent() {
        // arrange
        var order = mock(Order.class);
        when(order.getBuyerId()).thenReturn(buyerId);
        when(order.getOrderStatus()).thenReturn(OrderStatus.fromId(1L));
        var buyer = mock(Buyer.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(buyerRepository.findById(buyerId)).thenReturn(Optional.of(buyer));

        // act
        handler.handle(this.domainEvent);

        // assert
        verify(orderingIntegrationEventService).addAndSaveEvent(any(OrderStatusChangedToStockConfirmedIntegrationEvent.class));
    }

    @Test
    void throwsExceptionWhenOrderIsNotFound() {
        // arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // act - assert
        assertThrows(NoSuchElementException.class, () -> handler.handle(this.domainEvent));
    }

    @Test
    void throwsExceptionWhenBuyerIsNotFound() {
        // arrange
        var order = mock(Order.class);
        when(order.getBuyerId()).thenReturn(buyerId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(buyerRepository.findById(buyerId)).thenReturn(Optional.empty());

        // act - assert
        assertThrows(NoSuchElementException.class, () -> handler.handle(this.domainEvent));
    }
}
