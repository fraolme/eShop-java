package io.github.fraolme.services.catalog.integration_events.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;

import java.math.BigDecimal;

public class ProductPriceChangedIntegrationEvent extends IntegrationEvent {

    private final Long productId;
    private final BigDecimal newPrice;
    private final BigDecimal oldPrice;

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
