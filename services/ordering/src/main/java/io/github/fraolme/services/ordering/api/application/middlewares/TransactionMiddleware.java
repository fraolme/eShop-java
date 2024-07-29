package io.github.fraolme.services.ordering.api.application.middlewares;

import an.awesome.pipelinr.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TransactionMiddleware implements Command.Middleware {

    private static final Logger log = LoggerFactory.getLogger(TransactionMiddleware.class);

    @Transactional
    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        log.info("--- Begin Transaction for {} {}", command.getClass().getName(), command);
        R response = next.invoke();
        log.info("--- Commit Transaction for {}", command.getClass().getName());
        return response;
    }
}
