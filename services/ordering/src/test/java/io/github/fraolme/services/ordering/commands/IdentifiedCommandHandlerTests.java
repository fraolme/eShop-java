package io.github.fraolme.services.ordering.commands;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.commands.handlers.IdentifiedCommandHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IdentifiedCommandHandlerTests {

    @Mock
    Pipeline pipeline;

    //NOTE: was not able to test the success scenarios due to the peculiar way of pieplinr calling method command.execute(pipeline)
    @Test
    public void testHandleCommandException() {
        // arrange
        var userId = UUID.randomUUID();
        var commandHandler = new IdentifiedCommandHandler<CreateOrderCommand, Voidy>();
        CreateOrderCommand command = mock(CreateOrderCommand.class);
        when(command.userId()).thenReturn(userId);
        when(command.execute(pipeline)).thenThrow(new RuntimeException("Execution failed"));

        IdentifiedCommand<CreateOrderCommand, Voidy> identifiedCommand = new IdentifiedCommand<>(command, UUID.randomUUID());

        // act
        var result = commandHandler.handle(identifiedCommand);

        // assert
        assertNull(result);
    }
}
