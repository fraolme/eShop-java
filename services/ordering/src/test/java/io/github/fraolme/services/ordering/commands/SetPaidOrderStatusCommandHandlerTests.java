package io.github.fraolme.services.ordering.commands;

import io.github.fraolme.services.ordering.api.application.commands.SetPaidOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.SetPaidOrderStatusCommandHandler;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetPaidOrderStatusCommandHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    SetPaidOrderStatusCommandHandler handler;

    SetPaidOrderStatusCommand command;
    Long orderId = 7L;

    @BeforeEach
    void setUp(){
        this.command = new SetPaidOrderStatusCommand(this.orderId);
    }

    @Test
    public void orderStatusUpdatedSuccessfully() {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.of(order));

        // act
        handler.handle(command);

        // assert
        verify(order).setPaidStatus();
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
}
