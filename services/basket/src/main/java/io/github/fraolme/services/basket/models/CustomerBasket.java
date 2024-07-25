package io.github.fraolme.services.basket.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RedisHash
public class CustomerBasket {

    @Id
    private String buyerId;

    private List<BasketItem> items;

    public CustomerBasket() {
        this.items = new ArrayList<>();
    }

    public CustomerBasket(String customerId) {
        this.buyerId = customerId;
        this.items = new ArrayList<>();
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomerBasket that)) return false;
        return Objects.equals(buyerId, that.buyerId) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(buyerId, items);
    }
}
