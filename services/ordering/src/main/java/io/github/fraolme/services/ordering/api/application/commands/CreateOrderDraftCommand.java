package io.github.fraolme.services.ordering.api.application.commands;

import an.awesome.pipelinr.Command;
import io.github.fraolme.services.ordering.api.application.models.BasketItem;
import io.github.fraolme.services.ordering.api.application.models.OrderDraftDTO;
import java.util.List;

public record CreateOrderDraftCommand(
        String buyerId,
        List<BasketItem> items
) implements Command<OrderDraftDTO> {}