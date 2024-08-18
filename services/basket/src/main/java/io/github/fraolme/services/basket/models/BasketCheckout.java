package io.github.fraolme.services.basket.models;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BasketCheckout (
        String city,
        String street ,
        String state ,
        String country ,
        String zipCode ,
        String cardNumber ,
        String cardHolderName ,
        ZonedDateTime cardExpiration ,
        String cardSecurityNumber ,
        Long cardTypeId ,
        String buyer ,
        UUID requestId) {}
