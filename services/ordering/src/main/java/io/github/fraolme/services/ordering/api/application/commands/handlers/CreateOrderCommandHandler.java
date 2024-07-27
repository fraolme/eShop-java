package io.github.fraolme.services.ordering.api.application.commands.handlers;

import io.github.fraolme.services.ordering.api.application.commands.CreateOrderCommand;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.Address;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.axonframework.commandhandling.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateOrderCommandHandler {
    private final OrderRepository orderRepository;
    private static final Logger log = LoggerFactory.getLogger(CreateOrderCommandHandler.class);

    public CreateOrderCommandHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @CommandHandler
    public void handle(CreateOrderCommand cmd) {
        //TODO: add integration event to clean the basket

        var address = new Address(cmd.street(), cmd.city(), cmd.state(), cmd.country(), cmd.zipCode());
        var order = new Order(cmd.userId(), cmd.username(), address, cmd.cardTypeId(), cmd.cardNumber(),
                cmd.cardSecurityNumber(), cmd.cardHolderName(), cmd.cardExpiration(), null, null);

        for(var item : cmd.orderItems()) {
            order.addOrderItem(item.productId(), item.productName(), item.unitPrice(), item.discount(),
                    item.pictureUrl(), item.units());
        }

        //TODO: use structured logging with Serliog or others to log all properties of order
        log.info("--- Creating Order - Order: {}", order);

        //TODO: Unit Of Work to execute the saving of order with the Domain events in a single transaction
        orderRepository.save(order);
    }
}
