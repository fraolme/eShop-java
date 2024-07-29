package io.github.fraolme.services.ordering.api.application.commands;

import an.awesome.pipelinr.Command;
import java.util.UUID;

public record IdentifiedCommand<T extends Command<R>, R>(
        Command<R> command,
        UUID id
)implements Command<R> {}

