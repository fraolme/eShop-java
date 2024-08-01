package io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate;

import io.github.fraolme.services.ordering.domain.base.Enumeration;
import jakarta.persistence.*;

@Inheritance
@Entity
public class CardType extends Enumeration {

    public CardType() {}

    private CardType(Long id, String name) {
        super(id, name);
    }

    @Transient
    public static CardType amex = new CardType(1L, "Amex");

    @Transient
    public static CardType visa = new CardType(2L, "Visa");

    @Transient
    public static CardType masterCard = new CardType(3L, "MasterCard");

    public static CardType fromId(Long id) {
        return fromId(id, CardType.class);
    }
}
