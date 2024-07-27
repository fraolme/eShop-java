package io.github.fraolme.services.ordering;

import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

public class CommandHandlerTests {

    @MockBean
    OrderRepository orderRepository;

    @Test
    void testCancelOrderCommandHandler() {
        //
    }
}
