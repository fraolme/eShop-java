package io.github.fraolme.services.ordering.api.application.commands;

import io.github.fraolme.services.ordering.api.domain.base.ICommand;
import java.util.UUID;

public record IdentifiedCommand<T extends ICommand<R>, R>(
        ICommand<R> command,
        UUID id
)implements ICommand<R> {}

