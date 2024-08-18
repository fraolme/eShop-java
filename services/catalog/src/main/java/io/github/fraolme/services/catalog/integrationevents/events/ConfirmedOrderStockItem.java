package io.github.fraolme.services.catalog.integrationevents.events;

public record ConfirmedOrderStockItem(Long productId, boolean hasStock) {}
