package io.github.fraolme.services.basket.integrationevents.events;

import io.github.fraolme.event_bus_rabbitmq.events.IntegrationEvent;
import io.github.fraolme.services.basket.models.CustomerBasket;

import java.time.ZonedDateTime;
import java.util.UUID;

public class UserCheckoutAcceptedIntegrationEvent extends IntegrationEvent {

    private UUID userId;
    private String username;
    private String city;
    private String street;
    private String state;
    private String country;
    private String zipCode;
    private String cardNumber;
    private String cardHolderName;
    private ZonedDateTime cardExpiration;
    private String cardSecurityNumber;
    private Long cardTypeId;
    private String buyer;
    private UUID requestId;
    private CustomerBasket basket;

    public UserCheckoutAcceptedIntegrationEvent() {}

    public UserCheckoutAcceptedIntegrationEvent(UUID userId, String username, String city, String street,
                                                String state, String country, String zipCode, String cardNumber,
                                                String cardHolderName, ZonedDateTime cardExpiration, String cardSecurityNumber,
                                                Long cardTypeId, String buyer, UUID requestId, CustomerBasket basket) {
        this.userId = userId;
        this.username = username;
        this.city = city;
        this.street = street;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.cardExpiration = cardExpiration;
        this.cardSecurityNumber = cardSecurityNumber;
        this.cardTypeId = cardTypeId;
        this.buyer = buyer;
        this.requestId = requestId;
        this.basket = basket;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public ZonedDateTime getCardExpiration() {
        return cardExpiration;
    }

    public String getCardSecurityNumber() {
        return cardSecurityNumber;
    }

    public Long getCardTypeId() {
        return cardTypeId;
    }

    public String getBuyer() {
        return buyer;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public CustomerBasket getBasket() {
        return basket;
    }
}
