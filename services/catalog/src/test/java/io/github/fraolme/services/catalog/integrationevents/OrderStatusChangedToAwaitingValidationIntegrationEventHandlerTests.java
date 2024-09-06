package io.github.fraolme.services.catalog.integrationevents;

import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStockConfirmedIntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStockItem;
import io.github.fraolme.services.catalog.integrationevents.events.OrderStockRejectedIntegrationEvent;
import io.github.fraolme.services.catalog.integrationevents.handlers.OrderStatusChangedToAwaitingValidationIntegrationEventHandler;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderStatusChangedToAwaitingValidationIntegrationEventHandlerTests {

    @Mock
    CatalogItemRepository catalogItemRepository;

    @Mock
    CatalogIntegrationEventService catalogIntegrationEventService;

    @InjectMocks
    OrderStatusChangedToAwaitingValidationIntegrationEventHandler handler;

    @Test
    void sendsStockConfirmedIntegrationEvent() {
        // arrange
        var orderId = 2L;
        var itemId = 4L;
        var units = 30;
        var orderStockItems = new ArrayList<OrderStockItem>();
        orderStockItems.add(new OrderStockItem(itemId, units));
        var event = new OrderStatusChangedToAwaitingValidationIntegrationEvent(orderId, orderStockItems);

        var catalogItem = mock(CatalogItem.class);
        when(catalogItem.getAvailableStock()).thenReturn(units + 20);
        when(catalogItemRepository.findById(itemId)).thenReturn(Optional.of(catalogItem));
        // act
        handler.handle(event);
        // assert
        verify(catalogIntegrationEventService).saveEvent(any(OrderStockConfirmedIntegrationEvent.class));
        verify(catalogIntegrationEventService).saveEvent(any(OrderStockConfirmedIntegrationEvent.class));
    }

    @Test
    void sendsStockRejectedIntegrationEvent() {
        // arrange
        var orderId = 2L;
        var itemId = 4L;
        var units = 30;
        var orderStockItems = new ArrayList<OrderStockItem>();
        orderStockItems.add(new OrderStockItem(itemId, units));
        var event = new OrderStatusChangedToAwaitingValidationIntegrationEvent(orderId, orderStockItems);

        var catalogItem = mock(CatalogItem.class);
        when(catalogItem.getAvailableStock()).thenReturn(units - 20);
        when(catalogItemRepository.findById(itemId)).thenReturn(Optional.of(catalogItem));
        // act
        handler.handle(event);
        // assert
        verify(catalogIntegrationEventService).saveEvent(any(OrderStockRejectedIntegrationEvent.class));
        verify(catalogIntegrationEventService).publishThroughEventBus(any(OrderStockRejectedIntegrationEvent.class));
    }

    @Test
    void throwsExceptionWhenCatalogItemIsNotFound() {
        // arrange
        var orderId = 2L;
        var itemId = 4L;
        var units = 30;
        var orderStockItems = new ArrayList<OrderStockItem>();
        orderStockItems.add(new OrderStockItem(itemId, units));
        var event = new OrderStatusChangedToAwaitingValidationIntegrationEvent(orderId, orderStockItems);

        when(catalogItemRepository.findById(itemId)).thenReturn(Optional.empty());

        // act - assert
        assertThrows(NoSuchElementException.class, () -> handler.handle(event));
    }
}
