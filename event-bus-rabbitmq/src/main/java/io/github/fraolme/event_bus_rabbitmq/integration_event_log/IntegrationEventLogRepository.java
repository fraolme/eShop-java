package io.github.fraolme.event_bus_rabbitmq.integration_event_log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface IntegrationEventLogRepository extends JpaRepository<IntegrationEventLog, UUID> {

    List<IntegrationEventLog> findByTransactionIdAndState(String transactionId, EventState state);
}
