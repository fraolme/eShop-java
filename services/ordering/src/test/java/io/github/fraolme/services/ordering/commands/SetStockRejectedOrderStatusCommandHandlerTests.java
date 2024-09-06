package io.github.fraolme.services.ordering.commands;

import io.github.fraolme.services.ordering.api.application.commands.SetStockRejectedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.SetStockRejectedOrderStatusCommandHandler;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SetStockRejectedOrderStatusCommandHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    SetStockRejectedOrderStatusCommandHandler handler;

    SetStockRejectedOrderStatusCommand command;
    Long orderId = 7L;

    @BeforeEach
    void setUp(){
        this.command = new SetStockRejectedOrderStatusCommand(this.orderId, List.of(2L));
    }

    @Test
    public void orderStatusUpdatedSuccessfully() {
        // arrange
        Order order = mock(Order.class);
        when(orderRepository.findById(this.orderId)).thenReturn(Optional.of(order));

        // act
        handler.handle(command);

        // assert
        verify(order).setCancelledStatusWhenStockIsRejected(any());
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
