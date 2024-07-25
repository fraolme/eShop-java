package io.github.fraolme.services.basket.repositories;

import io.github.fraolme.services.basket.models.CustomerBasket;
import org.springframework.data.repository.CrudRepository;

public interface RedisBasketRepository extends CrudRepository<CustomerBasket, String> {
}
