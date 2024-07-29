package io.github.fraolme.services.ordering.api.application.commands;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Voidy;

public record ShipOrderCommand(
        Long orderNumber
) implements Command<Voidy> {}
