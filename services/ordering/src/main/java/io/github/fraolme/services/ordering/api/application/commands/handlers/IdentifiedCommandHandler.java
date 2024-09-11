package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import io.github.fraolme.services.ordering.infrastructure.idempotency.RequestManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IdentifiedCommandHandler<T extends Command<R>, R> implements Command.Handler<IdentifiedCommand<T, R>, R> {

    private static final Logger log = LoggerFactory.getLogger(IdentifiedCommandHandler.class);
    private final Pipeline pipeline;
    private final RequestManager requestManager;

    public IdentifiedCommandHandler(Pipeline pipeline, RequestManager requestManager) {
        this.pipeline = pipeline;
        this.requestManager = requestManager;
    }

    @Override
    public R handle(IdentifiedCommand<T,R> identifiedCommand) {
        try {
            // idempotency check
            var alreadyExists = requestManager.requestExists(identifiedCommand.id());
            if(alreadyExists) {
                return null;
            }
            var command = identifiedCommand.command();
            var commandName = command.getClass().getSimpleName();

            requestManager.createRequestForCommand(identifiedCommand.id(), commandName);

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
