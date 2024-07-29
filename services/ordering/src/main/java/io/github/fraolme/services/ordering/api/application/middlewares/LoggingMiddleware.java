package io.github.fraolme.services.ordering.api.application.middlewares;

import an.awesome.pipelinr.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingMiddleware implements Command.Middleware {

    private static final Logger log = LoggerFactory.getLogger(LoggingMiddleware.class);

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        log.info("--- Handling command {} ({})", command.getClass().getName(), command);
        R response = next.invoke();
        log.info("--- Command {} handled - response: {}", command.getClass().getName(), response);
        return response;
    }
}
