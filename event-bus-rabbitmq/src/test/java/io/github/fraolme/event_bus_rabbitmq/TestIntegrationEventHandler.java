package io.github.fraolme.event_bus_rabbitmq;

import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

public class TestIntegrationEventHandler implements IIntegrationEventHandler<TestIntegrationEvent> {

    private boolean handled;

    public TestIntegrationEventHandler() {
        this.handled = false;
    }

    public void handle(TestIntegrationEvent event) {
        this.handled = true;
    }

    public boolean getHandled() {
        return this.handled;
    }
}
