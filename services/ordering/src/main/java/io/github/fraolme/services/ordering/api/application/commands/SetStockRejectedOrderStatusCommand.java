package io.github.fraolme.services.ordering.api.application.commands;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;
import java.util.List;

public record SetStockRejectedOrderStatusCommand(
        Long orderNumber,
        List<Long> orderStockItems
) implements Command<Voidy> {}
