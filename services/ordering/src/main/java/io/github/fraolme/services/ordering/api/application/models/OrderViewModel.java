package io.github.fraolme.services.ordering.api.application.models;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public record OrderViewModel(Long orderNumber, ZonedDateTime date, String status, String description,
                             String street, String city, String zipCode, String country,
                             List<OrderItemViewModel> orderItems, BigDecimal total) {
}
