package io.github.fraolme.services.ordering.domain.events;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import java.time.ZonedDateTime;
import java.util.UUID;

public record OrderStartedDomainEvent(
        Order order,
        UUID userUuid,
        String username,
        CardType cardType,
        String cardNumber,
        String cardSecurityNumber,
        String cardHolderName,
        ZonedDateTime cardExpiration
) implements Notification {}
