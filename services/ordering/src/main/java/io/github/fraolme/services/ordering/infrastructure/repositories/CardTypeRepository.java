package io.github.fraolme.services.ordering.infrastructure.repositories;

import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {

}
