package io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate;

import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import io.github.fraolme.services.ordering.domain.base.Enumeration;
import jakarta.persistence.*;

import java.util.Objects;

@Inheritance
@Entity
public class OrderStatus extends Enumeration {
    public OrderStatus() {}

    private OrderStatus(Long id, String name) {
        super(id, name);
    }

    public static OrderStatus fromId(Long id) {
        return fromId(id, OrderStatus.class);
    }

    @Transient
    public static OrderStatus submitted = new OrderStatus(1L, "Submitted");
    @Transient
    public static OrderStatus awaitingValidation = new OrderStatus(2L, "Awaiting Validation");
    @Transient
    public static OrderStatus stockConfirmed = new OrderStatus(3L, "Stock Confirmed");
    @Transient
    public static OrderStatus paid = new OrderStatus(4L, "Paid");
    @Transient
    public static OrderStatus shipped = new OrderStatus(5L, "Shipped");
    @Transient
    public static OrderStatus cancelled = new OrderStatus(6L, "Cancelled");
}
