package io.github.fraolme.services.ordering.infrastructure.repositories;

import io.github.fraolme.services.ordering.infrastructure.idempotency.ClientRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientRequestRepository extends JpaRepository<ClientRequest, UUID> {
}
