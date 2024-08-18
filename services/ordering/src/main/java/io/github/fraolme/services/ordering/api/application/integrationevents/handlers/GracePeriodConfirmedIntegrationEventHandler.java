package io.github.fraolme.services.ordering.api.application.integrationevents.handlers;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.GracePeriodConfirmedIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GracePeriodConfirmedIntegrationEventHandler implements IIntegrationEventHandler<GracePeriodConfirmedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(GracePeriodConfirmedIntegrationEventHandler.class);
    private final Pipeline pipeline;

    public GracePeriodConfirmedIntegrationEventHandler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void handle(GracePeriodConfirmedIntegrationEvent event) {
        log.info("--- Handling Integration event: {} at Ordering Service - {}", event.getId(), event);

        var cmd = new SetAwaitingValidationOrderStatusCommand(event.getOrderId());
        log.info("--- Sending command: {} - Order Number: {} ({}}",
                SetAwaitingValidationOrderStatusCommand.class.getSimpleName(), cmd.orderNumber(), cmd);

        cmd.execute(pipeline);
    }
}
