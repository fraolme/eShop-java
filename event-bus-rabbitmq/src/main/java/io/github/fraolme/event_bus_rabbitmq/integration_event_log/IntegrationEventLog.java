package io.github.fraolme.event_bus_rabbitmq.integration_event_log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
public class IntegrationEventLog {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private String eventTypeName;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(nullable = false)
    private Integer timeSent;

    @Column(nullable = false)
    private ZonedDateTime creationTime;

    private String transactionId;

    public IntegrationEventLog() {}

    public IntegrationEventLog(IntegrationEvent event, String content, UUID transactionId) {
        this.eventId = event.getId();
        this.creationTime = event.getCreationDate();
        this.eventTypeName = event.getClass().getSimpleName();
        this.content = content;
        this.state = EventState.NotPublished;
        this.timeSent = 0;
        this.transactionId = transactionId.toString();
    }

    public IntegrationEvent parseContent() {
        try {
            Class<? extends IntegrationEvent> eventType = (Class<? extends IntegrationEvent>) Class.forName(this.eventTypeName);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(this.content, eventType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UUID getEventId() {
        return eventId;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public String getContent() {
        return content;
    }

    public EventState getState() {
        return state;
    }

    public Integer getTimeSent() {
        return timeSent;
    }

    public ZonedDateTime getCreationTime() {
        return creationTime;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public void setTimeSent(Integer timeSent) {
        this.timeSent = timeSent;
    }

    public String getEventTypeShortName() {
        var arr = this.eventTypeName.split("\\.");
        return arr[arr.length - 1];
    }
}
