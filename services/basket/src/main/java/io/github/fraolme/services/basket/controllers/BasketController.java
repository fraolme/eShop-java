package io.github.fraolme.services.basket.controllers;

import io.github.fraolme.event_bus_rabbitmq.EventBusRabbitMQ;
import io.github.fraolme.services.basket.integrationevents.events.UserCheckoutAcceptedIntegrationEvent;
import io.github.fraolme.services.basket.models.BasketCheckout;
import io.github.fraolme.services.basket.models.CustomerBasket;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import io.github.fraolme.services.basket.utils.UuidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;

@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "basket")
@RestController
public class BasketController {

    private final Logger log = LoggerFactory.getLogger(BasketCheckout.class);
    private final RedisBasketRepository redisBasketRepository;
    private final EventBusRabbitMQ eventBus;

    public BasketController(RedisBasketRepository redisBasketRepository, EventBusRabbitMQ eventBus) {
        this.redisBasketRepository = redisBasketRepository;
        this.eventBus = eventBus;
    }

    @GetMapping("{customerId}")
    public CustomerBasket getBasketById(@PathVariable String customerId) {
        return this.redisBasketRepository.findById(customerId)
                .orElse(new CustomerBasket(customerId));
    }

    @PostMapping()
    public CustomerBasket updateBasket(@RequestBody CustomerBasket customerBasket) {
        return this.redisBasketRepository.save(customerBasket);
    }

    @DeleteMapping("{customerId}")
    public void deleteBasketById(@PathVariable String customerId) {
        this.redisBasketRepository.deleteById(customerId);
    }

    @PostMapping("checkout")
    public void checkout(@RequestBody BasketCheckout basketCheckout, @RequestHeader("x-requestid") String requestIdFromHeader,
                         @AuthenticationPrincipal Jwt jwt) {

        String customerId = jwt.getClaim("sub");
        UUID requestId = UuidUtils.tryParseUUID(requestIdFromHeader).orElse(basketCheckout.requestId());

        var basket = this.redisBasketRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        var username = (String) jwt.getClaim("preferred_username");

        var eventMessage = new UserCheckoutAcceptedIntegrationEvent(UUID.fromString(customerId), username, basketCheckout.city(),
                basketCheckout.street(), basketCheckout.state(), basketCheckout.country(), basketCheckout.zipCode(),
                basketCheckout.cardNumber(), basketCheckout.cardHolderName(), basketCheckout.cardExpiration(),
                basketCheckout.cardSecurityNumber(), basketCheckout.cardTypeId(), basketCheckout.buyer(), basketCheckout.requestId(),
                basket);

        try {
            eventBus.publish(eventMessage);
        } catch (Exception ex) {
            log.error("ERROR publishing integration event: {} from Basket Service", eventMessage.getId(), ex);
            throw ex;
        }
    }
}
