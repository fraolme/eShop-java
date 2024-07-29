package io.github.fraolme.services.ordering.api.domain.aggregatesModel.buyerAggregate;

import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.util.Objects;

@Entity
public class CardType {

    @Id
    private Long id;

    @Column(length = 200, nullable = false)
    private String name;


    public CardType() {}

    public CardType(String name) {
        this.name = name;
    }

    public CardType(Long id) {
        this.id = id;
    }

    private CardType(Long id, String name) {
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
        if (!(o instanceof CardType that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Transient
    public static CardType amex = new CardType(1L, "Amex");

    @Transient
    public static CardType visa = new CardType(2L, "Visa");

    @Transient
    public static CardType masterCard = new CardType(3L, "MasterCard");
}
