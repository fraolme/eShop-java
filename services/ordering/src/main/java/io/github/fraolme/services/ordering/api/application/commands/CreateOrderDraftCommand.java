package io.github.fraolme.services.ordering.api.application.commands;

import io.github.fraolme.services.ordering.api.application.models.BasketItem;
import io.github.fraolme.services.ordering.api.application.models.OrderDraftDTO;
import io.github.fraolme.services.ordering.api.domain.base.ICommand;
import java.util.List;

public record CreateOrderDraftCommand(
        String buyerId,
        List<BasketItem> items
) implements ICommand<OrderDraftDTO> {}