package io.github.fraolme.services.ordering.commands;

import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.CancelOrderCommandHandler;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelOrderCommandHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    CancelOrderCommandHandler cancelOrderCommandHandler;

    CancelOrderCommand command;
    Long orderId = 7L;

    @BeforeEach
    void setUp(){
        this.command = new CancelOrderCommand(this.orderId);
    }

    @Test
    public void orderFoundAndCancelledSuccessfully() throws OrderingDomainException {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.of(order));

        // act
        cancelOrderCommandHandler.handle(command);

        // assert
        verify(order).setCancelledStatus(); // verify that setCancelledStatus was called
        verify(orderRepository).save(order); // verify that the order was saved
    }

    @Test
    public void orderNotFound() {
        // arrange
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.empty());

        // act
        cancelOrderCommandHandler.handle(command);

        // assert
        verify(orderRepository, never()).save(any(Order.class)); // verify that save was never called
    }

    @Test
    public void throwsRuntimeExceptionWhenOrderingDomainExceptionIsCaught() throws OrderingDomainException {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.of(order));
        doThrow(new OrderingDomainException("Test exception")).when(order).setCancelledStatus();

        // act & assert
        assertThrows(RuntimeException.class, () -> cancelOrderCommandHandler.handle(command));

        verify(orderRepository, never()).save(order); // verify that save was never called
    }
}
