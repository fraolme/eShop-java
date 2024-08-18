package io.github.fraolme.services.ordering.api.application.integrationevents.handlers;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderPaymentFailedIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderPaymentFailedIntegrationEventHandler implements IIntegrationEventHandler<OrderPaymentFailedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderPaymentFailedIntegrationEventHandler.class);
    private final Pipeline pipeline;

    public OrderPaymentFailedIntegrationEventHandler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void handle(OrderPaymentFailedIntegrationEvent event) {
        log.info("--- Handling Integration event: {} at Ordering Service - {}", event.getId(), event);

        var cmd = new CancelOrderCommand(event.getOrderId());
        log.info("--- Sending command: {} - Order Number: {} ({}}",
                CancelOrderCommand.class.getSimpleName(), cmd.orderNumber(), cmd);

        cmd.execute(pipeline);
    }
}
