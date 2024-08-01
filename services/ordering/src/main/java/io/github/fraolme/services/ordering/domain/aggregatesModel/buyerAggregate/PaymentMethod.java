package io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate;

import io.github.fraolme.services.ordering.domain.base.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.ZonedDateTime;

@Inheritance
@jakarta.persistence.Entity
public class PaymentMethod extends Entity {
    @Column(nullable = false, length = 200)
    private String alias;
    @Column(nullable = false, length = 25)
    private String cardNumber;
    private String securityNumber;
    @Column(nullable = false, length = 200)
    private String cardHolderName;
    @Column(nullable = false)
    private ZonedDateTime expiration;

    @ManyToOne(optional = false)
    private CardType cardType;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    public PaymentMethod() {}

    public PaymentMethod(CardType cardType, String alias, String cardNumber, String securityNumber,
                         String cardHolderName, ZonedDateTime expiration) {
        if(cardNumber == null || cardNumber.isEmpty()) {
            //TODO: throw new OrderingDomainException
        }
        if(securityNumber == null || securityNumber.isEmpty()) {
            //TODO: throw new OrderingDomainException
        }
        if(cardHolderName == null || cardHolderName.isEmpty()) {
            //TODO: throw new OrderingDomainException
        }
        if(expiration.isBefore(ZonedDateTime.now())) {
            //TODO: throw new OrderingDomainException
        }
        this.alias = alias;
        this.expiration = expiration;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.securityNumber = securityNumber;
        this.cardHolderName = cardHolderName;
    }

    public boolean isEqualTo(CardType cardType, String cardNumber, ZonedDateTime expiration) {
        return this.cardType.getId().equals(cardType.getId()) && this.cardNumber.equals(cardNumber) &&
                this.expiration.isEqual(expiration);
    }
}
