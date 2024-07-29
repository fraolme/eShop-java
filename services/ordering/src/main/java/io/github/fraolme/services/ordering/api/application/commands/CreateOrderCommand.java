package io.github.fraolme.services.ordering.api.application.commands;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.models.OrderItemDTO;
import java.time.ZonedDateTime;
import java.util.List;

public record CreateOrderCommand(
        List<OrderItemDTO> orderItems,
        String userId,
        String username,
        String city,
        String street,
        String state,
        String country,
        String zipCode,
        String cardNumber,
        String cardHolderName,
        ZonedDateTime cardExpiration,
        String cardSecurityNumber,
        Integer cardTypeId
) implements Command<Voidy> {}
