package io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.util.Objects;

@Entity
public class OrderStatus {
    @Id
    private Long id;

    private String name;

    public OrderStatus() {}

    public OrderStatus(String name) {
        this.name = name;
    }

    public OrderStatus(Long id) {
        this.id = id;
    }

    private OrderStatus(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStatus that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
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
