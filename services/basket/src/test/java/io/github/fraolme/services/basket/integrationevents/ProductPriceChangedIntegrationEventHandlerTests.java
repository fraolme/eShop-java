package io.github.fraolme.services.basket.integrationevents;

import io.github.fraolme.services.basket.integrationevents.events.ProductPriceChangedIntegrationEvent;
import io.github.fraolme.services.basket.integrationevents.handlers.ProductPriceChangedIntegrationEventHandler;
import io.github.fraolme.services.basket.models.BasketItem;
import io.github.fraolme.services.basket.models.CustomerBasket;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductPriceChangedIntegrationEventHandlerTests {

    @Mock
    RedisBasketRepository redisBasketRepository;

    @InjectMocks
    ProductPriceChangedIntegrationEventHandler handler;

    @Test
    void updatesProductPriceInMatchingBaskets() {
        // arrange
        var productId = 9L;
        var oldPrice = new BigDecimal("20.5");
        var newPrice = new BigDecimal("27.3");
        var event = new ProductPriceChangedIntegrationEvent(productId, oldPrice, newPrice);
        var baskets = new ArrayList<CustomerBasket>();
        var basket = new CustomerBasket();
        var item = new BasketItem();
        item.setId(UUID.randomUUID().toString());
        item.setUnitPrice(oldPrice);
        item.setProductId(productId);
        basket.setItems(List.of(item));
        baskets.add(basket);
        when(redisBasketRepository.findAll()).thenReturn(baskets);
        // act
        handler.handle(event);

        // assert
        verify(redisBasketRepository).save(basket);
    }
}
