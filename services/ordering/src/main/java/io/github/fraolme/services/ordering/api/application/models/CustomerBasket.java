package io.github.fraolme.services.ordering.api.application.models;

import java.util.List;

public class CustomerBasket {

    private String buyerId;
    private List<BasketItem> items;

    public CustomerBasket() {}

    public CustomerBasket(String buyerId, List<BasketItem> items) {
        this.buyerId = buyerId;
        this.items = items;
    }

    public List<BasketItem> getItems() {
        return items;
    }

    public void setItems(List<BasketItem> items) {
        this.items = items;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}
