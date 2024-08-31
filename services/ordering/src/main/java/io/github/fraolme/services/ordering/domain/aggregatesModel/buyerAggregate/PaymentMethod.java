package io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate;

import io.github.fraolme.services.ordering.domain.base.Entity;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import jakarta.persistence.*;

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

    // we will never access this, it is there just to create a foreign key relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    public PaymentMethod() {}

    public PaymentMethod(CardType cardType, String alias, String cardNumber, String securityNumber,
                         String cardHolderName, ZonedDateTime expiration, Buyer buyer) throws OrderingDomainException {
        if(cardNumber == null || cardNumber.isEmpty()) {
            throw new OrderingDomainException("CardNumber");
        }
        if(securityNumber == null || securityNumber.isEmpty()) {
            throw new OrderingDomainException("SecurityNumber");
        }
        if(cardHolderName == null || cardHolderName.isEmpty()) {
            throw new OrderingDomainException("CardHolderName");
        }
        if(expiration.isBefore(ZonedDateTime.now())) {
            throw new OrderingDomainException("Expiration");
        }
        this.alias = alias;
        this.expiration = expiration;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.securityNumber = securityNumber;
        this.cardHolderName = cardHolderName;
        this.buyer = buyer;
    }

    public boolean isEqualTo(CardType cardType, String cardNumber, ZonedDateTime expiration) {
        return this.cardType.getId().equals(cardType.getId()) && this.cardNumber.equals(cardNumber) &&
                this.expiration.isEqual(expiration);
    }
}
