package io.github.fraolme.services.basket.integrationevents.handlers;

import io.github.fraolme.event_bus_rabbitmq.events.IIntegrationEventHandler;
import io.github.fraolme.services.basket.integrationevents.events.ProductPriceChangedIntegrationEvent;
import io.github.fraolme.services.basket.models.CustomerBasket;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductPriceChangedIntegrationEventHandler implements IIntegrationEventHandler<ProductPriceChangedIntegrationEvent> {

    private final Logger log = LoggerFactory.getLogger(ProductPriceChangedIntegrationEventHandler.class);
    private final RedisBasketRepository redisBasketRepository;

    public ProductPriceChangedIntegrationEventHandler(RedisBasketRepository redisBasketRepository) {
        this.redisBasketRepository = redisBasketRepository;
    }

    @Override
    public void handle(ProductPriceChangedIntegrationEvent event) {
        log.info("--- Handling integration event: {} at Basket - {}", event.getId(), event);
        var baskets = redisBasketRepository.findAll();
        for(CustomerBasket basket: baskets) {
            updatePriceInBasketItems(event.getProductId(), event.getNewPrice(), event.getOldPrice(), basket);
        }
    }

    private void updatePriceInBasketItems(Long productId, BigDecimal newPrice, BigDecimal oldPrice, CustomerBasket basket) {
        var itemsToUpdate = basket.getItems().stream().filter(p -> p.getProductId().equals(productId)).toList();
        if(!itemsToUpdate.isEmpty()) {
            log.info("--- ProductPriceChangedIntegrationEventHandler - updating items in basket for user: {} ({})",
                    basket.getBuyerId(), itemsToUpdate);

            for(var item : itemsToUpdate) {
                if(item.getUnitPrice().equals(oldPrice)) {
                    var originalPrice = item.getUnitPrice();
                    item.setUnitPrice(newPrice);
                    item.setOldUnitPrice(originalPrice);
                }
            }

            redisBasketRepository.save(basket);
        }
    }
}
