package io.github.fraolme.event_bus_rabbitmq;

import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Scope("singleton")
public class InMemoryEventBusSubscriptionsManager {
    private final HashMap<String, List<Class<? extends IIntegrationEventHandler<? extends IntegrationEvent>>>> handlers;
    private final List<Class<? extends IntegrationEvent>> eventTypes;

    public InMemoryEventBusSubscriptionsManager() {
        handlers = new HashMap<>();
        eventTypes = new ArrayList<>();
    }

    public boolean isEmpty() {
        return this.handlers.isEmpty();
    }

    public void clear() {
        this.handlers.clear();
    }

    public void addSubscription(Class<? extends IntegrationEvent> eventClass,
                                Class<? extends IIntegrationEventHandler<? extends IntegrationEvent>> handler) {
        String eventName = eventClass.getSimpleName();
        if(!this.handlers.containsKey(eventName)) {
            handlers.put(eventName, new ArrayList<>());
        }

        if(this.handlers.get(eventName).contains(handler)) {
            throw new IllegalArgumentException(
                    String.format("Handler Type %s already registered for '%s'",
                            handler.getSimpleName(), eventName));
        }

        this.handlers.get(eventName).add(handler);
        if(!eventTypes.contains(eventClass)) {
            eventTypes.add(eventClass);
        }
    }

    public void removeSubscription(Class<? extends IntegrationEvent> eventClass,
                                   Class<? extends IIntegrationEventHandler> handler) {
        String eventName = eventClass.getSimpleName();
        this.handlers.get(eventName).remove(handler);
        if(this.handlers.get(eventName).isEmpty()) {
            handlers.remove(eventName);
            eventTypes.remove(eventClass);
        }
    }

    public boolean hasSubscriptionForEvent(Class<? extends IntegrationEvent> eventType) {
        return this.hasSubscriptionForEvent(eventType.getSimpleName());
    }

    public boolean hasSubscriptionForEvent(String eventName) {
        return this.handlers.containsKey(eventName);
    }

    public List<Class<? extends IIntegrationEventHandler<? extends IntegrationEvent>>> getHandlersForEvent(
            Class<? extends IntegrationEvent> eventType) {
        return this.getHandlersForEvent(eventType.getSimpleName());
    }

    public List<Class<? extends IIntegrationEventHandler<? extends IntegrationEvent>>> getHandlersForEvent(
            String eventName) {
        return this.handlers.get(eventName);
    }

    public Class<? extends IntegrationEvent> getEventTypeByName(String eventName) {
        return this.eventTypes.stream().filter(e -> e.getSimpleName().equals(eventName)).findFirst().get();
    }
}
