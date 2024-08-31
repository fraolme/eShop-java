package io.github.fraolme.services.ordering.infrastructure.eventdispatcher;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The Domain events stored at the entity will be sent here by JPA
 * This event handler dispatches domain events to the appropriate handler in pipelinr
 *  This event handler will be called before the entity is saved
 */
@Component
public class DomainEventDispatcher {

    private final Logger log = LoggerFactory.getLogger(DomainEventDispatcher.class);
    private final Pipeline pipeline;

    public DomainEventDispatcher(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @EventListener
    public void dispatchDomainEvent(Notification event) {
        log.info("--- Dispatching a domain event {} through the pipeline", event.getClass().getSimpleName());
        event.send(pipeline);
    }
}
