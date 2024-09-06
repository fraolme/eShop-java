package io.github.fraolme.services.ordering.domainevents;

import io.github.fraolme.services.ordering.api.application.domainEventHandlers.buyerAndPaymentMethodVerified.UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandler;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.Buyer;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.PaymentMethod;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.domain.events.BuyerAndPaymentMethodVerifiedDomainEvent;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandler handler;

    BuyerAndPaymentMethodVerifiedDomainEvent domainEvent;
    Long orderId = 7L;
    Long buyerId = 15L;
    Long paymentMethodId = 12L;

    @BeforeEach
    void setUp() throws OrderingDomainException {
        var buyer = new Buyer(UUID.randomUUID(), "username");
        buyer.setId(buyerId);
        var paymentMethod = new PaymentMethod(CardType.fromId(1L), "alias", "cardNumber", "secNumber", "cardHolder",
                ZonedDateTime.now().plusYears(1), buyer);
        paymentMethod.setId(paymentMethodId);
        this.domainEvent = new BuyerAndPaymentMethodVerifiedDomainEvent(buyer, paymentMethod, orderId);
    }

    @Test
    void successfullyUpdatesOrder() {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // act
        this.handler.handle(this.domainEvent);

        // assert
        verify(order).setBuyerId(buyerId);
        verify(order).setPaymentMethodId(paymentMethodId);
        verify(orderRepository).save(order);
    }

    @Test
    void throwsExceptionWhenOrderIsNotFound() {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // act - assert
        assertThrows(NoSuchElementException.class, () -> this.handler.handle(this.domainEvent));
    }
}
