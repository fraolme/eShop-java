package io.github.fraolme.services.ordering.backgroundtasks;

import an.awesome.pipelinr.Pipeline;
import io.github.fraolme.services.ordering.api.application.commands.SetAwaitingValidationOrderStatusCommand;
import io.github.fraolme.services.ordering.config.AppProperties;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GracePeriodManagerServiceTests {

    @Mock
    OrderRepository orderRepository;

    @Mock
    AppProperties appProperties;

    @Mock
    Pipeline pipeline;

    @InjectMocks
    GracePeriodManagerService gracePeriodManagerService;

    int gracePeriodTime = 5;
    Long orderIdInGracePeriod = 2L;

    @Test
    public void sendsCommandsWhenThereAreOrdersInGracePeriod() {
        // arrange
        when(appProperties.getGracePeriodTime()).thenReturn(gracePeriodTime);
        when(orderRepository.findOrderIdsWithGracePeriod(gracePeriodTime)).thenReturn(List.of(orderIdInGracePeriod));

        // act
        gracePeriodManagerService.execute();

        // assert
        verify(pipeline).send(any(SetAwaitingValidationOrderStatusCommand.class));
    }

    @Test
    public void doesNotSendCommandWhenThereIsNoGracePeriodOrders() {
        // arrange
        when(appProperties.getGracePeriodTime()).thenReturn(gracePeriodTime);
        when(orderRepository.findOrderIdsWithGracePeriod(gracePeriodTime)).thenReturn(List.of());

        // act
        gracePeriodManagerService.execute();

        // assert
        verify(pipeline, never()).send(any(SetAwaitingValidationOrderStatusCommand.class));
    }
}
