package io.github.fraolme.services.ordering.domain;

import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Address;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderItem;
import io.github.fraolme.services.ordering.domain.events.OrderStartedDomainEvent;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class OrderAggregateTests {

    @Test
    public void createOrderItemSucceeds() throws OrderingDomainException {
        // arrange
        var productId = 1L;
        var productName = "FakeProductName";
        var unitPrice = BigDecimal.valueOf(12);
        var discount = BigDecimal.valueOf(15);
        var pictureUrl = "FakeUrl";
        var units = 5;

        // act
        var fakeOrderItem = new OrderItem(null, productId, productName, unitPrice, discount, pictureUrl, units);

        // assert
        assertNotNull(fakeOrderItem);
    }

    @Test
    public void createOrderItemFailsForInvalidNumberOfUnits() throws OrderingDomainException {
        var productId = 1L;
        var productName = "FakeProductName";
        var unitPrice = BigDecimal.valueOf(12);
        var discount = BigDecimal.valueOf(15);
        var pictureUrl = "FakeUrl";
        var units = -1;

        assertThrows(OrderingDomainException.class, () -> new OrderItem(null, productId, productName,
                unitPrice, discount, pictureUrl, units));
    }

    @Test
    public void createOrderItemFailsWhenTotalPriceOfItemsIsLessThanDiscountApplied() throws OrderingDomainException {
        var productId = 1L;
        var productName = "FakeProductName";
        var unitPrice = BigDecimal.valueOf(12);
        var discount = BigDecimal.valueOf(15);
        var pictureUrl = "FakeUrl";
        var units = 1;

        assertThrows(OrderingDomainException.class, () -> new OrderItem(null, productId, productName,
                unitPrice, discount, pictureUrl, units));
    }

    @Test
    public void invalidDiscountSettingFails() throws OrderingDomainException {
        var productId = 1L;
        var productName = "FakeProductName";
        var unitPrice = BigDecimal.valueOf(12);
        var discount = BigDecimal.valueOf(15);
        var pictureUrl = "FakeUrl";
        var units = 5;

        var fakeOrderItem = new OrderItem(null, productId, productName, unitPrice, discount, pictureUrl, units);

        assertThrows(OrderingDomainException.class, () -> fakeOrderItem.setNewDiscount(BigDecimal.valueOf(-1)));
    }

    @Test
    public void invalidUnitSettingFails() throws OrderingDomainException {
        var productId = 1L;
        var productName = "FakeProductName";
        var unitPrice = BigDecimal.valueOf(12);
        var discount = BigDecimal.valueOf(15);
        var pictureUrl = "FakeUrl";
        var units = 5;

        var fakeOrderItem = new OrderItem(null, productId, productName, unitPrice, discount, pictureUrl, units);

        assertThrows(OrderingDomainException.class, () -> fakeOrderItem.addUnits(-1));
    }

    @Test
    public void addingTwoTimesOnTheSameItemShouldMakeTheOrderTotalSumOfTheTwoItems() throws OrderingDomainException {
        var address = new Address("street", "city", "state", "country", "zipcode");
        var order = new Order(UUID.randomUUID(), "fakeName", address, 2L, "12", "123", "name",
                ZonedDateTime.now(), null, null);
        order.addOrderItem(1L, "cup", BigDecimal.valueOf(10), BigDecimal.ZERO, "", 1);
        order.addOrderItem(1L, "cup", BigDecimal.valueOf(10), BigDecimal.ZERO, "", 1);

        assertEquals(BigDecimal.valueOf(20), order.getTotal());
    }

    @Test
    public void addNewOrderRaisesOrderStartedEvent() throws OrderingDomainException {
        var address = new Address("street", "city", "state", "country", "zipcode");
        var order = new Order(UUID.randomUUID(), "fakeName", address, 2L, "12", "123", "name",
                ZonedDateTime.now(), null, null);

        assertEquals(order.getDomainEvents().size(), 1);
    }


    @Test
    public void addDomainEventExplicitlyRaisesEvent() throws OrderingDomainException {
        var street = "fakeStreet";
        var city = "FakeCity";
        var state = "fakeState";
        var country = "fakeCountry";
        var zipcode = "FakeZipCode";
        var cardTypeId = 2L;
        var cardNumber = "12";
        var cardSecurityNumber = "123";
        var cardHolderName = "FakeName";
        var cardExpiration = ZonedDateTime.now().plusYears(1);
        var identity = UUID.randomUUID();

        var address = new Address(street, city, state, country, zipcode);
        var order = new Order(identity, "fakeName", address, cardTypeId, cardNumber, cardSecurityNumber,
                cardHolderName, cardExpiration, null, null);

        order.addDomainEvent(new OrderStartedDomainEvent(order, identity, "fakeName", CardType.fromId(cardTypeId),
                cardNumber, cardSecurityNumber, cardHolderName, cardExpiration));

        assertEquals(order.getDomainEvents().size(), 2);
    }

    @Test
    public void removeDomainEventShouldRemoveEvent() throws OrderingDomainException {
        // arrange
        var street = "fakeStreet";
        var city = "FakeCity";
        var state = "fakeState";
        var country = "fakeCountry";
        var zipcode = "FakeZipCode";
        var cardTypeId = 2L;
        var cardNumber = "12";
        var cardSecurityNumber = "123";
        var cardHolderName = "FakeName";
        var cardExpiration = ZonedDateTime.now().plusYears(1);
        var identity = UUID.randomUUID();

        var address = new Address(street, city, state, country, zipcode);
        var order = new Order(identity, "fakeName", address, cardTypeId, cardNumber, cardSecurityNumber,
                cardHolderName, cardExpiration, null, null);

        var event = new OrderStartedDomainEvent(order, identity, "fakeName", CardType.fromId(cardTypeId),
                cardNumber, cardSecurityNumber, cardHolderName, cardExpiration);
        // act
        order.addDomainEvent(event);
        order.removeDomainEvent(event);
        // assert
        assertEquals(order.getDomainEvents().size(), 1);
    }
}
