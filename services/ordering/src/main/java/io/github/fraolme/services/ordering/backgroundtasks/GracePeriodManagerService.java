package io.github.fraolme.services.ordering.backgroundtasks;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.config.AppProperties;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GracePeriodManagerService {

    private final Logger log = LoggerFactory.getLogger(GracePeriodManagerService.class);
    private final OrderRepository orderRepository;
    private final AppProperties appProperties;
    private final Pipeline pipeline;

    public GracePeriodManagerService(OrderRepository orderRepository, AppProperties appProperties, Pipeline pipeline) {
        this.orderRepository = orderRepository;
        this.appProperties = appProperties;
        this.pipeline = pipeline;
    }

    @Scheduled(fixedDelay = 30_000)
    public void execute() {
        log.debug("GracePeriodManagerService background task is doing background work.");
        var orderIds = orderRepository.findOrderIdsWithGracePeriod(this.appProperties.getGracePeriodTime());
        for(var orderId: orderIds) {
            var cmd = new SetAwaitingValidationOrderStatusCommand(orderId);
            log.info("--- Sending command: {} - Order Number: {} ({}}",
                    SetAwaitingValidationOrderStatusCommand.class.getSimpleName(), cmd.orderNumber(), cmd);

            cmd.execute(pipeline);
        }
    }
}