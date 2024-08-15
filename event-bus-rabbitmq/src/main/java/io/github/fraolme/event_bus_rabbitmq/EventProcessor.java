package io.github.fraolme.event_bus_rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class EventProcessor {

    private final Logger log = LoggerFactory.getLogger(EventProcessor.class);
    private final InMemoryEventBusSubscriptionsManager subscriptionsManager;
    private final ObjectMapper objectMapper;
    private ApplicationContext applicationContext;

    public EventProcessor(InMemoryEventBusSubscriptionsManager subscriptionsManager, ObjectMapper objectMapper,
                          ApplicationContext applicationContext) {
        this.subscriptionsManager = subscriptionsManager;
        this.objectMapper = objectMapper;
        this.applicationContext = applicationContext;
    }

    public void process(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            processEvent(routingKey, messageBody);
        } catch (Exception e) {
            log.warn("----Error Processing Message {}", message, e);
        }
        // message processing acknowledgement is automatically sent by the rabbitmq library
    }

    private void processEvent(String eventName, String message) {
        log.trace("Processing RabbitMQ event: {}", eventName);
        log.info("Message Body: {}", message);
        if(this.subscriptionsManager.hasSubscriptionForEvent(eventName)) {
            var handlers = subscriptionsManager.getHandlersForEvent(eventName);
            for(var handler: handlers) {
                Class<? extends IntegrationEvent> eventType = subscriptionsManager.getEventTypeByName(eventName);
                try {
                    IntegrationEvent integrationEvent = objectMapper.readValue(message, eventType);
                    IIntegrationEventHandler handlerInstance = applicationContext.getBean(handler);
                    handlerInstance.handle(integrationEvent);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            log.warn("Not subscription for RabbitMQ event: {}", eventName);
        }
    }
}
