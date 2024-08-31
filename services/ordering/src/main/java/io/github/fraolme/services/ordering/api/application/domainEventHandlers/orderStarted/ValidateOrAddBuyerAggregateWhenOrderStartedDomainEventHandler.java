package io.github.fraolme.services.ordering.api.application.domainEventHandlers.orderStarted;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStatusChangedToSubmittedIntegrationEvent;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.Buyer;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import io.github.fraolme.services.ordering.domain.events.OrderStartedDomainEvent;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;

@Component
public class ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler
    implements Notification.Handler<OrderStartedDomainEvent> {

    private final Logger log = LoggerFactory.getLogger(ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler.class);
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    public ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler(BuyerRepository buyerRepository,
                                                                         OrderingIntegrationEventService orderingIntegrationEventService) {
        this.buyerRepository = buyerRepository;
        this.orderingIntegrationEventService = orderingIntegrationEventService;
    }

    @Override
    public void handle(OrderStartedDomainEvent event) {
        CardType cardType = event.cardType() != null ? event.cardType() : CardType.masterCard;

        var buyer = buyerRepository.findByIdentityUuid(event.userUuid())
                .orElse(new Buyer(event.userUuid(), event.username()));

        try {
            buyer.verifyOrAddPaymentMethod(cardType, String.format("Payment Method on %s", ZonedDateTime.now()),
                    event.cardNumber(),
                    event.cardSecurityNumber(),
                    event.cardHolderName(),
                    event.cardExpiration(),
                    event.order().getId());
        } catch (OrderingDomainException e) {
            throw new RuntimeException(e);
        }

        buyerRepository.saveAndFlush(buyer);

        var integrationEvent = new OrderStatusChangedToSubmittedIntegrationEvent(event.order().getId(),
                event.order().getOrderStatus().getName(), buyer.getName());
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent);

        log.trace("Buyer {} and related payment method were validated or updated for orderId: {}",
                buyer.getId(), event.order().getId());
    }


}
