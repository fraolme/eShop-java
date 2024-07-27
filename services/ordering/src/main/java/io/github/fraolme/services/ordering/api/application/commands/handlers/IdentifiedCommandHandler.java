package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import io.github.fraolme.services.ordering.api.domain.base.ICommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IdentifiedCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderCommandHandler.class);
    private final CommandGateway commandGateway;

    public IdentifiedCommandHandler(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @CommandHandler
    public <T extends ICommand<R>, R> R handle(IdentifiedCommand<T,R> identifiedCommand) {
        //TODO: idempotency check check if request is already processed by uuid

        //TODO: save request for idempotency check later

        try {
            var command = identifiedCommand.command();
            var commandName = command.getClass().getName();
            var idProperty = "";
            var commandId = "";

            if (command instanceof CreateOrderCommand createOrderCommand) {
                idProperty = "userId";
                commandId = createOrderCommand.userId();
            } else if (command instanceof CancelOrderCommand cancelOrderCommand) {
                idProperty = "orderNumber";
                commandId = cancelOrderCommand.orderNumber().toString();
            } else if (command instanceof ShipOrderCommand shipOrderCommand) {
                idProperty = "orderNumber";
                commandId = shipOrderCommand.orderNumber().toString();
            } else {
                idProperty = "Id?";
                commandId = "N/A";
            }

            log.info("--- Sending Command: {} - {}: {} ({})", commandName, idProperty, commandId, command);
            var result = commandGateway.sendAndWait(command);
            log.info("--- Command Result: {} - {} - {}: {} ({})", result, commandName, idProperty, commandId, command);

            return (R) result;
        } catch (Exception ignored) {
            return null;
        }
    }
}
