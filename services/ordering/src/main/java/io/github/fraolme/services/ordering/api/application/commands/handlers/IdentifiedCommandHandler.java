package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdentifiedCommandHandler<T extends Command<R>, R> implements Command.Handler<IdentifiedCommand<T, R>, R> {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderCommandHandler.class);

    @Autowired
    private Pipeline pipeline;

    public IdentifiedCommandHandler() {
    }

    @Override
    public R handle(IdentifiedCommand<T,R> identifiedCommand) {
        //TODO: idempotency check check if request is already processed by uuid

        //TODO: save request for idempotency check later

        try {
            var command = identifiedCommand.command();
            var commandName = command.getClass().getName();
            var idProperty = "";
            var commandId = "";

            if (command instanceof CreateOrderCommand createOrderCommand) {
                idProperty = "userId";
                commandId = createOrderCommand.userId().toString();
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
            var result = command.execute(pipeline);
            log.info("--- Command Result: {} - {} - {}: {} ({})", result, commandName, idProperty, commandId, command);

            return result;
        } catch (Exception ignored) {
            return null;
        }
    }
}
