package io.github.fraolme.services.ordering.domainevents;

import io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderStarted.ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStatusChangedToSubmittedIntegrationEvent;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.Buyer;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Address;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.domain.events.OrderStartedDomainEvent;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandlerTests {

    @Mock
    BuyerRepository buyerRepository;

    @Mock
    OrderingIntegrationEventService orderingIntegrationEventService;

    @InjectMocks
    ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler handler;

    OrderStartedDomainEvent domainEvent;
    UUID userUuid;

    @BeforeEach
    void setUp() {
        userUuid = UUID.randomUUID();
        var order = new Order(userUuid, "username", new Address(), 1L, "cardNumber", "cardSecurityNumber", "cardHolder",
                ZonedDateTime.now().plusYears(1), null, null);
        domainEvent = new OrderStartedDomainEvent(order, userUuid, "username", CardType.fromId(1L),
                "cardNumber", "cardSecurityNumber", "cardHolder", ZonedDateTime.now().plusYears(1L));
    }

    @Test
    void successfullyAddsOrValidatesBuyer() {
        // arrange
        var buyer = mock(Buyer.class);
        when(buyerRepository.findByIdentityUuid(userUuid)).thenReturn(Optional.of(buyer));

        // act
        handler.handle(domainEvent);

        // assert
        verify(buyerRepository).saveAndFlush(buyer);
        verify(orderingIntegrationEventService).addAndSaveEvent(any(OrderStatusChangedToSubmittedIntegrationEvent.class));
    }

    @Test
    void handlesOrderingDomainException() throws OrderingDomainException {
        // arrange
        var buyer = mock(Buyer.class);
        when(buyerRepository.findByIdentityUuid(userUuid)).thenReturn(Optional.of(buyer));
        doThrow(new OrderingDomainException("")).when(buyer).verifyOrAddPaymentMethod(any(), any(), any(), any(), any(), any(), any());

        // act - assert
        assertThrows(RuntimeException.class, () -> handler.handle(domainEvent));
    }
}
