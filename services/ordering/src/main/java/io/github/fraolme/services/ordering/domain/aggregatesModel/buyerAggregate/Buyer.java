package io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate;

import io.github.fraolme.services.ordering.domain.base.Entity;
import io.github.fraolme.services.ordering.domain.base.IAggregateRoot;
import io.github.fraolme.services.ordering.domain.events.BuyerAndPaymentMethodVerifiedDomainEvent;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Inheritance
@jakarta.persistence.Entity
@NamedEntityGraph(name = "buyer_entity_graph", attributeNodes = {
        @NamedAttributeNode("paymentMethods")})
public class Buyer extends Entity implements IAggregateRoot {

    @Column(length = 200, nullable = false, unique = true)
    private UUID identityUuid;
    private String name;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private final List<PaymentMethod> paymentMethods;

    public Buyer() {
        this.paymentMethods = new ArrayList<PaymentMethod>();
    }

    public Buyer(UUID identity, String name) {
        this();
        if(identity == null) {
            throw new IllegalArgumentException("identity");
        }
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name");
        }
        this.identityUuid = identity;
        this.name = name;
    }

    public UUID getIdentityUuid() {
        return identityUuid;
    }

    public String getName() {
        return name;
    }

    public List<PaymentMethod> getPaymentMethods() {
        return Collections.unmodifiableList(paymentMethods);
    }

    public PaymentMethod verifyOrAddPaymentMethod(CardType cardType, String alias, String cardNumber,
                                                  String securityNumber, String cardHolderName, ZonedDateTime expiration,
                                                  Long orderId) throws OrderingDomainException {
        var existingPayment = paymentMethods.stream().filter(p -> p.isEqualTo(cardType, cardNumber, expiration))
                .findFirst();
        if(existingPayment.isPresent()) {
            this.addDomainEvent(new BuyerAndPaymentMethodVerifiedDomainEvent(this, existingPayment.get(), orderId));
            return existingPayment.get();
        }

        var payment = new PaymentMethod(cardType, alias, cardNumber, securityNumber, cardHolderName, expiration, this);
        paymentMethods.add(payment);

        this.addDomainEvent(new BuyerAndPaymentMethodVerifiedDomainEvent(this, payment, orderId));

        return payment;
    }
}
