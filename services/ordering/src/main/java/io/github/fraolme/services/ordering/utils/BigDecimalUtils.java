package io.github.fraolme.services.ordering.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    // d1 < d2
    public static boolean lessThan(BigDecimal d1, BigDecimal d2) {
        if(d1 == null || d2 == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }

        return d1.compareTo(d2) < 0;
    }

    // d1 > d2
    public static boolean greaterThan(BigDecimal d1, BigDecimal d2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("Arguments can't be null");
        }

        return d1.compareTo(d2) > 0;
    }
}
