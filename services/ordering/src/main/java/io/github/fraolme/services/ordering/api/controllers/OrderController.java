package io.github.fraolme.services.ordering.api.controllers;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Voidy;
import io.github.fraolme.services.ordering.api.application.commands.CancelOrderCommand;
import io.github.fraolme.services.ordering.api.application.commands.CreateOrderDraftCommand;
import io.github.fraolme.services.ordering.api.application.commands.IdentifiedCommand;
import io.github.fraolme.services.ordering.api.application.commands.ShipOrderCommand;
import io.github.fraolme.services.ordering.api.application.models.EnumerationViewModel;
import io.github.fraolme.services.ordering.api.application.models.OrderDraftDTO;
import io.github.fraolme.services.ordering.api.application.models.OrderSummaryViewModel;
import io.github.fraolme.services.ordering.api.application.models.OrderViewModel;
import io.github.fraolme.services.ordering.api.application.queries.OrderQueries;
import io.github.fraolme.services.ordering.utils.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@Validated
@RestController
@RequestMapping("order")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final Pipeline pipeline;
    private final OrderQueries orderQueries;

    public OrderController(Pipeline pipeline, OrderQueries orderQueries) {
        this.pipeline = pipeline;
        this.orderQueries = orderQueries;
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

    @PutMapping("cancel")
    public void cancelOrder(@RequestBody CancelOrderCommand cmd, @RequestHeader("x-requestid") String requestIdFromHeader) {

        UUID requestId = UuidUtils.tryParseUUID(requestIdFromHeader)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        var requestCancelOrder = new IdentifiedCommand<CancelOrderCommand, Voidy>(cmd, requestId);
        log.info("--- Sending command: {} - {}: {} ({})",
                requestCancelOrder.getClass().getName(),
                "orderNumber",
                requestCancelOrder.command().orderNumber(),
                requestCancelOrder);

        requestCancelOrder.execute(pipeline);
    }

    @PutMapping("ship")
    public void shipOrder(@RequestBody ShipOrderCommand cmd, @RequestHeader("x-requestid") String requestIdFromHeader) {

        UUID requestId = UuidUtils.tryParseUUID(requestIdFromHeader)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        var requestShipOrder = new IdentifiedCommand<ShipOrderCommand, Voidy>(cmd, requestId);

        log.info("--- Sending command: {} - {}: {} ({})",
                requestShipOrder.getClass().getName(),
                "orderNumber",
                requestShipOrder.command().orderNumber(),
                requestShipOrder);

        requestShipOrder.execute(pipeline);
    }

    @GetMapping("{orderId}")
    public OrderViewModel getOrder(@PathVariable Long orderId) {
        return orderQueries.getOrder(orderId);
    }

    @GetMapping
    public List<OrderSummaryViewModel> getOrders(@AuthenticationPrincipal Jwt jwt) {
        String userIdStr = jwt.getClaim("sub");
        var userId = UUID.fromString(userIdStr);
        return orderQueries.getOrdersByUser(userId);
    }

    @GetMapping("cardTypes")
    public List<EnumerationViewModel> getCardTypes() {
        return orderQueries.getCardTypes();
    }
}
