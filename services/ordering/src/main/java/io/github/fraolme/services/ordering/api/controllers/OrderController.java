package io.github.fraolme.services.ordering.api.controllers;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderDraftCommand;
import io.github.fraolme.services.ordering.api.application.models.OrderDraftDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final Pipeline pipeline;

    public OrderController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("draft")
    public OrderDraftDTO createOrderDraftFromBasket(@RequestBody CreateOrderDraftCommand createOrderDraftCommand) {
        log.info("--- Sending command: {} - {}: {} ({})",
                createOrderDraftCommand.getClass().getName(),
                "buyerId",
                createOrderDraftCommand.buyerId(),
                createOrderDraftCommand);

        return createOrderDraftCommand.execute(pipeline);
    }
}
