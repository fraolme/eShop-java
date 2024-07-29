package io.github.fraolme.services.ordering.api.domain.repositories;

import io.github.fraolme.services.ordering.api.domain.aggregatesModel.buyerAggregate.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Optional<Buyer> findByIdentityUuid(UUID identityUuid);
}
