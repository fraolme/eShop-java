package io.github.fraolme.services.basket.controllers;

import io.github.fraolme.services.basket.models.BasketCheckout;
import io.github.fraolme.services.basket.models.CustomerBasket;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import io.github.fraolme.services.basket.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

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
    public void checkout(@RequestBody BasketCheckout basketCheckout, @RequestHeader("x-requestid") String requestIdFromHeader) {
        // TODO: get customer id from bearer token
        String customerId = "1";
        UUID requestId = UuidUtils.tryParseUUID(requestIdFromHeader).orElse(basketCheckout.RequestId());

        var basket = this.redisBasketRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        // TODO: get username from bearer token

        // TODO: send UserCheckoutAcceptedIntegrationEvent
    }
}
