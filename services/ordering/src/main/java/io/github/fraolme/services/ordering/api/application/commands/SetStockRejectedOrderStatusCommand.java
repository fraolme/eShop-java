package io.github.fraolme.services.ordering.api.application.commands;

import io.github.fraolme.services.ordering.api.domain.base.ICommand;

import java.util.List;

public record SetStockRejectedOrderStatusCommand(
        Long orderNumber,
        List<Long> orderStockItems
) implements ICommand<Void> {}
