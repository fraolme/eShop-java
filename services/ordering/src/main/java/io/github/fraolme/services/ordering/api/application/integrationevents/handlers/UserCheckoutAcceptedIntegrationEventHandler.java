package io.github.fraolme.services.ordering.api.application.integrationevents.handlers;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.UserCheckoutAcceptedIntegrationEvent;
import io.github.fraolme.services.ordering.api.application.models.BasketItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserCheckoutAcceptedIntegrationEventHandler implements IIntegrationEventHandler<UserCheckoutAcceptedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(UserCheckoutAcceptedIntegrationEventHandler.class);
    private final Pipeline pipeline;

    public UserCheckoutAcceptedIntegrationEventHandler(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    /*
    * Integration event handler which starts the create order process
    * */
    @Override
    public void handle(UserCheckoutAcceptedIntegrationEvent event) {
        log.info("--- Handling integration event: {} at Ordering Service - ({})", event.getId(), event);

        if(event.getRequestId() != null) {
            var createOrderCmd = new CreateOrderCommand(
                    event.getBasket().getItems().stream().map(BasketItem::toOrderItemDTO).collect(Collectors.toList()),
                    event.getUserId(), event.getUsername(), event.getCity(), event.getStreet(), event.getState(),
                    event.getCountry(), event.getZipCode(), event.getCardNumber(), event.getCardHolderName(),
                    event.getCardExpiration(), event.getCardSecurityNumber(), event.getCardTypeId());

            var requestCreateOrder = new IdentifiedCommand<CreateOrderCommand, Voidy> (createOrderCmd, event.getRequestId());

            log.info("--- Sending command: {} - Id: {} ({})",
                    IdentifiedCommand.class.getSimpleName(),
                    requestCreateOrder.id(),
                    requestCreateOrder);

            var resp = requestCreateOrder.execute(pipeline);

            if(resp == null) {
                log.warn("CreateOrderCommand failed - RequestId: {}", event.getRequestId());
            } else {
                log.info("--- CreateOrderCommand succeeded - RequestId: {}", event.getRequestId());
            }
        } else {
            log.warn("Invalid Integration Event - RequestId is missing - {}", event);
        }
    }
}
