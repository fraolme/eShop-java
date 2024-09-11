package io.github.fraolme.services.ordering.infrastructure.idempotency;

import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import io.github.fraolme.services.ordering.infrastructure.repositories.ClientRequestRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class RequestManager {

    private final ClientRequestRepository clientRequestRepository;

    public RequestManager(ClientRequestRepository repository) {
        this.clientRequestRepository = repository;
    }

    public boolean requestExists(UUID id) {
        return this.clientRequestRepository.existsById(id);
    }

    public void createRequestForCommand(UUID id, String commandType) throws OrderingDomainException {

        var exists = requestExists(id);
        if(exists) {
            throw new OrderingDomainException(String.format("Request with %s already exists", id.toString()));
        }

        var request = new ClientRequest(id, commandType, ZonedDateTime.now());
        this.clientRequestRepository.saveAndFlush(request);
    }
}
