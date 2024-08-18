package io.github.fraolme.services.catalog.integrationevents.handlers;

import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.exceptions.CatalogDomainException;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class OrderStatusChangedToPaidIntegrationEventHandler
        implements IIntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(OrderStatusChangedToPaidIntegrationEventHandler.class);
    private final CatalogItemRepository catalogItemRepository;

    public OrderStatusChangedToPaidIntegrationEventHandler(CatalogItemRepository catalogItemRepository) {
        this.catalogItemRepository = catalogItemRepository;
    }

    @Override
    public void handle(OrderStatusChangedToPaidIntegrationEvent event) {
        log.info("--- Handling integration event: {} at Catalog Service - {}", event.getId(), event);

        var catalogItems = new ArrayList<CatalogItem>();
        for(var item : event.getOrderStockItems()) {
            var catalogItem = catalogItemRepository.findById(item.productId()).get();
            catalogItems.add(catalogItem);
            try {
                catalogItem.removeStock(item.units());
            } catch (CatalogDomainException e) {
                throw new RuntimeException(e);
            }
        }

        catalogItemRepository.saveAllAndFlush(catalogItems);
    }
}
