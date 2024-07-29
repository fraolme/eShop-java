package io.github.fraolme.services.ordering.api.application.models;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record OrderSummaryViewModel (Long orderNumber, ZonedDateTime date, String status, BigDecimal total){
}
