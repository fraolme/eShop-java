package io.github.fraolme.services.ordering.api.application.integrationevents.handlers;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.commands.SetStockRejectedOrderStatusCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.ConfirmedOrderStockItem;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStockRejectedIntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderStockRejectedIntegrationEventHandler implements IIntegrationEventHandler<OrderStockRejectedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStockRejectedIntegrationEventHandler.class);
    private final Pipeline pipeline;

    public OrderStockRejectedIntegrationEventHandler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void handle(OrderStockRejectedIntegrationEvent event) {
        log.info("--- Handling Integration event: {} at Ordering Service - {}", event.getId(), event);

        var orderStockRejectedItems = event.getOrderStockItems().stream()
                .filter(s -> !s.hasStock())
                .map(ConfirmedOrderStockItem::productId)
                .toList();

        var cmd = new SetStockRejectedOrderStatusCommand(event.getOrderId(), orderStockRejectedItems);
        log.info("--- Sending command: {} - Order Number: {} ({}}",
                SetStockRejectedOrderStatusCommand.class.getSimpleName(), cmd.orderNumber(), cmd);

        cmd.execute(pipeline);
    }
}