package io.github.fraolme.services.ordering.domain;

import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.Buyer;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.PaymentMethod;
import io.github.fraolme.services.ordering.domain.events.BuyerAndPaymentMethodVerifiedDomainEvent;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BuyerAggregateTests {

    @Test
    public void createBuyerSucceeds() {
        // arrange
        var identity = UUID.randomUUID();
        var name = "fakeUser";

        // act
        var fakeBuyerItem = new Buyer(identity, name);

        // asset
        assertNotNull(fakeBuyerItem);
    }

    @Test
    public void createBuyerFailsForInvalidIdentity() {
        UUID identity = null;
        String name = "fakeUser";

        assertThrows(IllegalArgumentException.class, () -> new Buyer(identity, name));
    }

    @Test
    public void addPaymentSucceeds() throws OrderingDomainException {
        // arrange
        var cardType = CardType.fromId(1L);
        var alias = "fakeAlias";
        var cardNumber = "124";
        var securityNumber = "1234";
        var cardHolderName = "FakeHolderName";
        var expiration = ZonedDateTime.now().plusYears(1);
        var orderId = 1L;
        var name = "fakeUser";
        var identity = UUID.randomUUID();
        var fakeBuyerItem = new Buyer(identity, name);

        // act
        var result = fakeBuyerItem.verifyOrAddPaymentMethod(cardType, alias, cardNumber, securityNumber, cardHolderName,
                expiration, orderId);

        // assert
        assertNotNull(result);
        assertEquals(fakeBuyerItem.getDomainEvents().size(), 1);
        assertEquals(fakeBuyerItem.getDomainEvents().get(0).getClass(), BuyerAndPaymentMethodVerifiedDomainEvent.class);
    }

    @Test
    public void createPaymentMethodSucceeds() throws OrderingDomainException {
        // arrange
        var cardType = CardType.fromId(1L);
        var alias = "fakeAlias";
        var cardNumber = "124";
        var securityNumber = "1234";
        var cardHolderName = "FakeHolderName";
        var expiration = ZonedDateTime.now().plusYears(1);

        // act
        var paymentMethod = new PaymentMethod(cardType, alias, cardNumber, securityNumber, cardHolderName, expiration);

        // assert
        assertNotNull(paymentMethod);
    }

    @Test
    public void createPaymentMethodFailsWhenExpirationPasses() throws OrderingDomainException {
        // arrange
        var cardType = CardType.fromId(1L);
        var alias = "fakeAlias";
        var cardNumber = "124";
        var securityNumber = "1234";
        var cardHolderName = "FakeHolderName";
        var expiration = ZonedDateTime.now().plusYears(-1);

        // act - assert
        assertThrows(OrderingDomainException.class, () -> new PaymentMethod(cardType, alias, cardNumber,
                securityNumber, cardHolderName, expiration));
    }

    @Test
    public void paymentMethodEquality() throws OrderingDomainException {
        // arrange
        var cardType = CardType.fromId(1L);
        var alias = "fakeAlias";
        var cardNumber = "124";
        var securityNumber = "1234";
        var cardHolderName = "FakeHolderName";
        var expiration = ZonedDateTime.now().plusYears(1);

        // act
        var paymentMethod = new PaymentMethod(cardType, alias, cardNumber, securityNumber, cardHolderName, expiration);
        var result = paymentMethod.isEqualTo(cardType, cardNumber, expiration);

        // assert
        assertTrue(result);
    }
}
