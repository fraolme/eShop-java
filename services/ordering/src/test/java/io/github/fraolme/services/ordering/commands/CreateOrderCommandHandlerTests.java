package io.github.fraolme.services.ordering.commands;

import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.CreateOrderCommandHandler;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStartedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.models.OrderItemDTO;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateOrderCommandHandlerTests {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderingIntegrationEventService orderingIntegrationEventService;

    @InjectMocks
    CreateOrderCommandHandler createOrderCommandHandler;

    CreateOrderCommand command;

    @BeforeEach
    void setUp() {
        var orderItems = List.of(new OrderItemDTO(1L, "Chair", new BigDecimal("19.5"), new BigDecimal("0"), 2, "" ));
        this.command = new CreateOrderCommand(orderItems, UUID.randomUUID(), "jj", "NN", "NN", "NN", "UU", "000",
                "111", "JJ", ZonedDateTime.now().plusYears(1), "777", 1L);
    }

    //NOTE: couldn't mock order since it is created in the method
    @Test
    void orderCreatedSuccessfully() {
       // act
       this.createOrderCommandHandler.handle(this.command);

       // assert
       verify(orderingIntegrationEventService).addAndSaveEvent(any(OrderStartedIntegrationEvent.class));
       verify(orderRepository).save(any(Order.class));
    }

}
