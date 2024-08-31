package io.github.fraolme.event_bus_rabbitmq.integration_event_log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import java.util.Optional;
import java.util.UUID;

public class IntegrationEventLogService {

    private final IntegrationEventLogRepository repository;
    private final ObjectMapper objectMapper;

    public IntegrationEventLogService(IntegrationEventLogRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    public Optional<IntegrationEventLog> getEventLog(UUID id) {
        return repository.findById(id);
    }

    public void saveEvent(IntegrationEvent event) {
        try {
            var content = objectMapper.writeValueAsString(event);
            var eventLog = new IntegrationEventLog(event, content);
            repository.saveAndFlush(eventLog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void markEventAsPublished(UUID eventId) {
        updateEventStatus(eventId, EventState.Published);
    }

    public void markEventAsInProgress(UUID eventId) {
        updateEventStatus(eventId, EventState.InProgress);
    }

    public void markEventAsFailed(UUID eventId) {
        updateEventStatus(eventId, EventState.PublishedFailed);
    }

    public IntegrationEvent parse(IntegrationEventLog eventLog) {
        try {
            Class<? extends IntegrationEvent> eventType = (Class<? extends IntegrationEvent>) Class.forName(eventLog.getEventTypeName());
            return objectMapper.readValue(eventLog.getContent(), eventType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateEventStatus(UUID eventId, EventState status) {
        var entry = repository.findById(eventId).get();
        entry.setState(status);

        if(status == EventState.InProgress) {
            entry.setTimeSent(entry.getTimeSent() + 1);
        }

        repository.saveAndFlush(entry);
    }
}
