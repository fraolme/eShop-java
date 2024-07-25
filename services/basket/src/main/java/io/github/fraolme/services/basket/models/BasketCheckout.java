package io.github.fraolme.services.basket.models;

import java.time.ZonedDateTime;
import java.util.UUID;

public record BasketCheckout (
        String City,
        String Street ,
        String State ,
        String Country ,
        String ZipCode ,
        String CardNumber ,
        String CardHolderName ,
        ZonedDateTime CardExpiration ,
        String CardSecurityNumber ,
        int CardTypeId ,
        String Buyer ,
        UUID RequestId) {}
