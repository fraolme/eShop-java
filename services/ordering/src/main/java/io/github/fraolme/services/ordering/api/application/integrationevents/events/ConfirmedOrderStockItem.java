package io.github.fraolme.services.ordering.api.application.integrationevents.events;

public record ConfirmedOrderStockItem(Long productId, boolean hasStock) {}
