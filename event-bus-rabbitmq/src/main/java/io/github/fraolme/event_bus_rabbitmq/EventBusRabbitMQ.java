package io.github.fraolme.event_bus_rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class EventBusRabbitMQ {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final Logger log = LoggerFactory.getLogger(EventBusRabbitMQ.class);
    private final InMemoryEventBusSubscriptionsManager subscriptionsManager;
    private final Queue queue;
    private final ObjectMapper objectMapper;
    private final RabbitAdmin rabbitAdmin;

    public EventBusRabbitMQ(RabbitTemplate rabbitTemplate, DirectExchange directExchange, Queue queue,
                            InMemoryEventBusSubscriptionsManager subscriptionsManager, ObjectMapper objectMapper,
                            RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.queue = queue;
        this.subscriptionsManager = subscriptionsManager;
        this.objectMapper = objectMapper;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void publish(IntegrationEvent event) {
        try {
            String routingKey = event.getClass().getSimpleName();
            var content = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(directExchange.getName(), routingKey, content);
            log.trace("Publishing event to RabbitMQ: {}", event.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(Class<? extends IntegrationEvent> eventType,
                          Class<? extends IIntegrationEventHandler<? extends IntegrationEvent>> handler) {
        String eventName = eventType.getSimpleName();
        if(!this.subscriptionsManager.hasSubscriptionForEvent(eventType)) {
            var binding = BindingBuilder.bind(this.queue).to(this.directExchange).with(eventName);
            rabbitAdmin.declareBinding(binding);
        }
        subscriptionsManager.addSubscription(eventType, handler);
    }

}
