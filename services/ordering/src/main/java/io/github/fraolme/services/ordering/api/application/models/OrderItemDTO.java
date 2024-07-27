package io.github.fraolme.services.ordering.api.application.models;

import java.math.BigDecimal;

public record OrderItemDTO (
        Long productId,
        String productName,
        BigDecimal unitPrice,
        BigDecimal discount,
        Integer units,
        String pictureUrl
){}
