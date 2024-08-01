package io.github.fraolme.services.ordering.api.application.models;

import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record OrderDraftDTO(List<OrderItemDTO> orderItems, BigDecimal total) {

    public static OrderDraftDTO fromOrder(Order order) {
        return new OrderDraftDTO(
          order.getOrderItems().stream().map(oi -> new OrderItemDTO(
                  oi.getProductId(),
                  oi.getOrderItemProductName(),
                  oi.getUnitPrice(),
                  oi.getDiscount(),
                  oi.getUnits(),
                  oi.getPictureUrl()
          )).collect(Collectors.toList()),
                order.getTotal()
        );
    }
}
