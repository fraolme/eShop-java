package io.github.fraolme.services.basket.utils;

import java.util.Optional;
import java.util.UUID;

public class UuidUtils {

    public static Optional<UUID> tryParseUUID(String uuidStr) {
            try {
                return Optional.of(UUID.fromString(uuidStr));
            } catch (IllegalArgumentException ignored){
                return Optional.empty();
            }
    }
}
