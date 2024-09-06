package io.github.fraolme.services.ordering.commands;

import io.github.fraolme.services.ordering.api.application.commands.SetStockConfirmedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.SetStockConfirmedOrderStatusCommandHandler;
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
public class SetStockConfirmedOrderStatusCommandHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    SetStockConfirmedOrderStatusCommandHandler handler;

    SetStockConfirmedOrderStatusCommand command;
    Long orderId = 7L;

    @BeforeEach
    void setUp(){
        this.command = new SetStockConfirmedOrderStatusCommand(this.orderId);
    }

    @Test
    public void orderStatusUpdatedSuccessfully() {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.of(order));

        // act
        handler.handle(command);

        // assert
        verify(order).setStockConfirmedStatus();
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
