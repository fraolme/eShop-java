package io.github.fraolme.services.ordering.api.application.integrationevents.handlers;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.commands.SetStockConfirmedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStockConfirmedIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderStockConfirmedIntegrationEventHandler implements IIntegrationEventHandler<OrderStockConfirmedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStockConfirmedIntegrationEventHandler.class);
    private final Pipeline pipeline;

    public OrderStockConfirmedIntegrationEventHandler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void handle(OrderStockConfirmedIntegrationEvent event) {
        log.info("--- Handling Integration event: {} at Ordering Service - {}", event.getId(), event);

        var cmd = new SetStockConfirmedOrderStatusCommand(event.getOrderId());
        log.info("--- Sending command: {} - Order Number: {} ({}}",
                SetStockConfirmedOrderStatusCommand.class.getSimpleName(), cmd.orderNumber(), cmd);

        cmd.execute(pipeline);
    }
}
