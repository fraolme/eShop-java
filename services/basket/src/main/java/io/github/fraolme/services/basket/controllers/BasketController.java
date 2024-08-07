package io.github.fraolme.services.basket.controllers;

import io.github.fraolme.services.basket.models.BasketCheckout;
import io.github.fraolme.services.basket.models.CustomerBasket;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import io.github.fraolme.services.basket.utils.UuidUtils;
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

    private final RedisBasketRepository redisBasketRepository;

    public BasketController(RedisBasketRepository redisBasketRepository) {
        this.redisBasketRepository = redisBasketRepository;
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
        UUID requestId = UuidUtils.tryParseUUID(requestIdFromHeader).orElse(basketCheckout.RequestId());

        var basket = this.redisBasketRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        var username = jwt.getClaim("preferred_username");

        // TODO: send UserCheckoutAcceptedIntegrationEvent
    }
}
