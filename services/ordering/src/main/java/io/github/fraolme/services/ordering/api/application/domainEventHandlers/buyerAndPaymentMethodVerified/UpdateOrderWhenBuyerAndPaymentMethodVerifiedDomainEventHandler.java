package io.github.fraolme.services.ordering.api.application.domainEventHandlers.buyerAndPaymentMethodVerified;

import an.awesome.pipelinr.Notification;
import io.github.fraolme.services.ordering.domain.events.BuyerAndPaymentMethodVerifiedDomainEvent;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandler
        implements Notification.Handler<BuyerAndPaymentMethodVerifiedDomainEvent> {

    private final Logger log = LoggerFactory.getLogger(UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandler.class);
    private final OrderRepository orderRepository;

    public UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public void handle(BuyerAndPaymentMethodVerifiedDomainEvent event) {
        var orderToUpdate = orderRepository.findById(event.orderId()).get();
        orderToUpdate.setBuyerId(event.buyer().getId());
        orderToUpdate.setPaymentMethodId(event.payment().getId());
        orderRepository.save(orderToUpdate);

        log.trace("Order with Id: {} has been successfully updated with a payment method {}",
                event.orderId(),
                event.payment());
    }
}
