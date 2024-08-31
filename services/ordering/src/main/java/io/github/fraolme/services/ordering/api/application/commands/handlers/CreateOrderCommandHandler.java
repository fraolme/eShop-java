package io.github.fraolme.services.ordering.api.application.commands.handlers;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.application.integrationevents.OrderingIntegrationEventService;
import io.github.fraolme.services.ordering.api.application.integrationevents.events.OrderStartedIntegrationEvent;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Address;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderCommandHandler implements Command.Handler<CreateOrderCommand, Voidy>{

    private static final Logger log = LoggerFactory.getLogger(CreateOrderCommandHandler.class);
    private final OrderRepository orderRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    public CreateOrderCommandHandler(OrderRepository orderRepository, OrderingIntegrationEventService orderingIntegrationEventService) {
        this.orderRepository = orderRepository;
        this.orderingIntegrationEventService = orderingIntegrationEventService;
    }

    @Override
    public Voidy handle(CreateOrderCommand cmd) {
        // add integration event to clean the basket
        var orderStartedIntegrationEvent = new OrderStartedIntegrationEvent(cmd.userId());
        this.orderingIntegrationEventService.addAndSaveEvent(orderStartedIntegrationEvent);

        var address = new Address(cmd.street(), cmd.city(), cmd.state(), cmd.country(), cmd.zipCode());
        var order = new Order(cmd.userId(), cmd.username(), address, cmd.cardTypeId(), cmd.cardNumber(),
                cmd.cardSecurityNumber(), cmd.cardHolderName(), cmd.cardExpiration(), null, null);

        for(var item : cmd.orderItems()) {
            try {
                order.addOrderItem(item.productId(), item.productName(), item.unitPrice(), item.discount(),
                        item.pictureUrl(), item.units());
            } catch (OrderingDomainException e) {
                throw new RuntimeException(e);
            }
        }

        //TODO: use structured logging with Serilog or others to log all properties of order
        log.info("--- Creating Order - Order: {}", order);

        //TODO: Unit Of Work to execute the saving of order with the Domain events in a single transaction
        orderRepository.save(order);

        return new Voidy();
    }
}
