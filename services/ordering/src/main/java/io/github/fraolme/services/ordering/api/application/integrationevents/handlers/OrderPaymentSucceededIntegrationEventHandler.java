package io.github.fraolme.services.ordering.api.application.integrationevents.handlers;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.commands.SetPaidOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderPaymentSucceededIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderPaymentSucceededIntegrationEventHandler implements IIntegrationEventHandler<OrderPaymentSucceededIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderPaymentSucceededIntegrationEventHandler.class);
    private final Pipeline pipeline;

    public OrderPaymentSucceededIntegrationEventHandler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void handle(OrderPaymentSucceededIntegrationEvent event) {
        log.info("--- Handling Integration event: {} at Ordering Service - {}", event.getId(), event);

        var cmd = new SetPaidOrderStatusCommand(event.getOrderId());
        log.info("--- Sending command: {} - Order Number: {} ({}}",
                SetPaidOrderStatusCommand.class.getSimpleName(), cmd.orderNumber(), cmd);

        cmd.execute(pipeline);
    }
}
