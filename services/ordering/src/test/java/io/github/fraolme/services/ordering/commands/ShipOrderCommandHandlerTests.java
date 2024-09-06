package io.github.fraolme.services.ordering.commands;

import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.ShipOrderCommandHandler;
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
public class ShipOrderCommandHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    ShipOrderCommandHandler handler;

    ShipOrderCommand command;
    Long orderId = 7L;

    @BeforeEach
    void setUp(){
        this.command = new ShipOrderCommand(this.orderId);
    }

    @Test
    public void orderFoundAndShippedSuccessfully() throws OrderingDomainException {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.of(order));

        // act
        handler.handle(command);

        // assert
        verify(order).setShippedStatus();
        verify(orderRepository).save(order);
    }

    @Test
    public void orderNotFound() {
        // arrange
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.empty());

        // act
        handler.handle(command);

        // assert
        verify(orderRepository, never()).save(any(Order.class)); // verify that save was never called
    }

    @Test
    public void throwsRuntimeExceptionWhenOrderingDomainExceptionIsCaught() throws OrderingDomainException {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.of(order));
        doThrow(new OrderingDomainException("Test exception")).when(order).setShippedStatus();

        // act & assert
        assertThrows(RuntimeException.class, () -> handler.handle(command));

        verify(orderRepository, never()).save(order); // verify that save was never called
    }
}
