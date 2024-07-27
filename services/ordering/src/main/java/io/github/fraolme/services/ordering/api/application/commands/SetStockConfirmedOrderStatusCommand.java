package io.github.fraolme.services.ordering.api.application.commands;

import io.github.fraolme.services.ordering.api.domain.base.ICommand;

public record SetStockConfirmedOrderStatusCommand(
        Long orderNumber
) implements ICommand<Void> {}
