package io.github.fraolme.services.catalog.integrationevents.handlers;

import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.CatalogIntegrationEventService;
import io.github.fraolme.services.catalog.integrationevents.events.ConfirmedOrderStockItem;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStockConfirmedIntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStockRejectedIntegrationEvent;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OrderStatusChangedToAwaitingValidationIntegrationEventHandler
        implements IIntegrationEventHandler<OrderStatusChangedToAwaitingValidationIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusChangedToAwaitingValidationIntegrationEventHandler.class);
    private final CatalogIntegrationEventService catalogIntegrationEventService;
    private final CatalogItemRepository catalogItemRepository;

    public OrderStatusChangedToAwaitingValidationIntegrationEventHandler(CatalogItemRepository catalogItemRepository,
            CatalogIntegrationEventService catalogIntegrationEventService) {
        this.catalogIntegrationEventService = catalogIntegrationEventService;
        this.catalogItemRepository = catalogItemRepository;
    }

    @Override
    public void handle(OrderStatusChangedToAwaitingValidationIntegrationEvent event) {
        log.info("--- Handling Integration event: {} at Catalog Service - {}", event.getId(), event);
        var confirmedOrderStockItems = new ArrayList<ConfirmedOrderStockItem>();
        for(var item: event.getOrderStockItems()) {
            var catalogItem = catalogItemRepository.findById(item.productId()).get();
            var hasStock = catalogItem.getAvailableStock() >= item.units();
            var confirmedOrderStockItem = new ConfirmedOrderStockItem(catalogItem.getId(), hasStock);
            confirmedOrderStockItems.add(confirmedOrderStockItem);
        }

        IntegrationEvent confirmedIntegrationEvent =  confirmedOrderStockItems.stream().anyMatch(s -> !s.hasStock())
                ? new OrderStockRejectedIntegrationEvent(event.getOrderId(), confirmedOrderStockItems)
                : new OrderStockConfirmedIntegrationEvent(event.getOrderId());

        catalogIntegrationEventService.saveEvent(confirmedIntegrationEvent);
        catalogIntegrationEventService.publishThroughEventBus(confirmedIntegrationEvent);
    }
}
