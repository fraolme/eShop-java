package io.github.fraolme.event_bus_rabbitmq.events;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class IntegrationEvent {
    private UUID id;
    private ZonedDateTime creationDate;

    public IntegrationEvent(UUID id, ZonedDateTime creationDate) {
        this.id = id;
        this.creationDate = creationDate;
    }

    public IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.creationDate = ZonedDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntegrationEvent that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
