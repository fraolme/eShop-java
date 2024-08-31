package io.github.fraolme.services.catalog.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

import java.math.BigDecimal;

public class ProductPriceChangedIntegrationEvent extends IntegrationEvent {

    private Long productId;
    private BigDecimal newPrice;
    private BigDecimal oldPrice;

    public ProductPriceChangedIntegrationEvent() {}

    public ProductPriceChangedIntegrationEvent(Long productId, BigDecimal newPrice, BigDecimal oldPrice) {
        this.productId = productId;
        this.newPrice = newPrice;
        this.oldPrice = oldPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }
}
