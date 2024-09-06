package io.github.fraolme.services.catalog.integrationevents;

import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.exceptions.CatalogDomainException;
import io.github.fraolme.services.catalog.integrationevents.events.*;
import io.github.fraolme.services.catalog.integrationevents.handlers.OrderStatusChangedToAwaitingValidationIntegrationEventHandler;
import io.github.fraolme.services.catalog.integrationevents.handlers.OrderStatusChangedToPaidIntegrationEventHandler;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderStatusChangedToPaidIntegrationEventHandlerTests {

    @Mock
    CatalogItemRepository catalogItemRepository;

    @InjectMocks
    OrderStatusChangedToPaidIntegrationEventHandler handler;

    @Test
    void removesPaidForItemsFromCatalog() throws CatalogDomainException {
        // arrange
        var orderId = 2L;
        var itemId = 4L;
        var units = 30;
        var orderStockItems = new ArrayList<OrderStockItem>();
        orderStockItems.add(new OrderStockItem(itemId, units));
        var event = new OrderStatusChangedToPaidIntegrationEvent(orderId, orderStockItems);

        var catalogItem = mock(CatalogItem.class);
        when(catalogItemRepository.findById(itemId)).thenReturn(Optional.of(catalogItem));
        // act
        handler.handle(event);
        // assert
        verify(catalogItem).removeStock(units);
        verify(catalogItemRepository).saveAllAndFlush(argThat(items -> {
            List<CatalogItem> itemList = StreamSupport.stream(items.spliterator(), false)
                    .toList();
            return itemList.size() == 1 && itemList.contains(catalogItem);
        }));
    }

    @Test
    void throwsRuntimeExceptionWhenCatalogDomainExceptionIsCaught() throws CatalogDomainException {
        // arrange
        var orderId = 2L;
        var itemId = 4L;
        var units = 30;
        var orderStockItems = new ArrayList<OrderStockItem>();
        orderStockItems.add(new OrderStockItem(itemId, units));
        var event = new OrderStatusChangedToPaidIntegrationEvent(orderId, orderStockItems);

        var catalogItem = mock(CatalogItem.class);
        when(catalogItemRepository.findById(itemId)).thenReturn(Optional.of(catalogItem));
        doThrow(CatalogDomainException.class).when(catalogItem).removeStock(50);

        // act - assert
        assertThrows(RuntimeException.class, () -> handler.handle(event));
    }

    @Test
    void throwsExceptionWhenCatalogItemIsNotFound() {
        // arrange
        var orderId = 2L;
        var itemId = 4L;
        var units = 30;
        var orderStockItems = new ArrayList<OrderStockItem>();
        orderStockItems.add(new OrderStockItem(itemId, units));
        var event = new OrderStatusChangedToPaidIntegrationEvent(orderId, orderStockItems);

        when(catalogItemRepository.findById(itemId)).thenReturn(Optional.empty());
        // act - assert
        assertThrows(NoSuchElementException.class, () -> handler.handle(event));
    }
}
