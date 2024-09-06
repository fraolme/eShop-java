package io.github.fraolme.services.ordering.commands;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.IdentifiedCommandHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IdentifiedCommandHandlerTests {

    @Mock
    Pipeline pipeline;

    @InjectMocks
    IdentifiedCommandHandler handler;

    @Test
    public void sendsCreateOrderCommand() {
        // arrange
        var userId = UUID.randomUUID();
        CreateOrderCommand command = new CreateOrderCommand(List.of(), userId, "username", "city", "street",
                "state", "country", "zipcode", "cardnumber", "cardholder",
                ZonedDateTime.now().plusYears(1), "cardsecuritynumber", 1L);
        var requestId = UUID.randomUUID();
        IdentifiedCommand<CreateOrderCommand, Voidy> identifiedCommand = new IdentifiedCommand<>(command, requestId);

        // act
        handler.handle(identifiedCommand);

        // assert
        verify(pipeline).send(any(CreateOrderCommand.class));
    }

    @Test
    public void sendsCancelOrderCommand() {
        // arrange
        var orderId = 7L;
        CancelOrderCommand command = new CancelOrderCommand(orderId);
        var requestId = UUID.randomUUID();
        IdentifiedCommand<CancelOrderCommand, Voidy> identifiedCommand = new IdentifiedCommand<>(command, requestId);
        // act
        handler.handle(identifiedCommand);

        // assert
        verify(pipeline).send(any(CancelOrderCommand.class));
    }

    @Test
    public void sendsShipOrderCommand() {
        // arrange
        var orderId = 7L;
        ShipOrderCommand command = new ShipOrderCommand(orderId);
        var requestId = UUID.randomUUID();
        IdentifiedCommand<ShipOrderCommand, Voidy> identifiedCommand = new IdentifiedCommand<>(command, requestId);

        // act
        handler.handle(identifiedCommand);

        // assert
        verify(pipeline).send(any(ShipOrderCommand.class));
    }

    @Test
    public void handlesExceptionsThrownInTheCommandHandlers() {
        // arrange
        var command = new ShipOrderCommand(10L);
        when(command.execute(pipeline)).thenThrow(new RuntimeException("Execution failed"));

        IdentifiedCommand<ShipOrderCommand, Voidy> identifiedCommand = new IdentifiedCommand<>(command, UUID.randomUUID());

        // act
        var result = handler.handle(identifiedCommand);

        // assert
        assertNull(result);
    }
}
