package io.github.fraolme.event_bus_rabbitmq;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryEventBusSubscriptionsManagerTests {

    @Test
    void shouldBeEmptyAfterCreation() {
        var manager = new InMemoryEventBusSubscriptionsManager();
        assertTrue(manager.isEmpty());
    }

    @Test
    void afterOneEventSubscriptionShouldContainTheEvent() {
        var manager = new InMemoryEventBusSubscriptionsManager();
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        assertTrue(manager.hasSubscriptionForEvent(TestIntegrationEvent.class));
    }

    @Test
    void afterAllSubscriptionsAreDeletedEventShouldNoLongerExists() {
        var manager = new InMemoryEventBusSubscriptionsManager();
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        manager.removeSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        assertFalse(manager.hasSubscriptionForEvent(TestIntegrationEvent.class));
    }

    @Test
    void getHandlersForEventShouldReturnAllHandlers() {
        var manager = new InMemoryEventBusSubscriptionsManager();
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationOtherEventHandler.class);
        var handlers = manager.getHandlersForEvent(TestIntegrationEvent.class);
        assertEquals(2, handlers.size());
    }
}
