package io.github.fraolme.services.ordering.domain;

import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Address;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ValueObjectTests {

    @Test
    public void twoAddressesWithSameValuesAreEqual() {
        var address1 = new Address("21st Street", "Madrid", "Madrid", "Spain", "0000");
        var address2 = new Address("21st Street", "Madrid", "Madrid", "Spain", "0000");
        assertEquals(address1, address2);
    }
}
