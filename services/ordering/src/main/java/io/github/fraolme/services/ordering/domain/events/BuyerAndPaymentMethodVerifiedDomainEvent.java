package io.github.fraolme.services.ordering.domain.events;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.Buyer;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.PaymentMethod;

public record BuyerAndPaymentMethodVerifiedDomainEvent (Buyer buyer, PaymentMethod payment, Long orderId)
        implements Notification {}
