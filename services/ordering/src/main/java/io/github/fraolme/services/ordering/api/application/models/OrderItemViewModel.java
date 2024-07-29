package io.github.fraolme.services.ordering.api.application.models;

import java.math.BigDecimal;

public record OrderItemViewModel(String productName, Integer units, BigDecimal unitPrice, String pictureUrl)
{}
