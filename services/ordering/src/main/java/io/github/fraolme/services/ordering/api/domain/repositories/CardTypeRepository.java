package io.github.fraolme.services.ordering.api.domain.repositories;

import io.github.fraolme.services.ordering.api.domain.aggregatesModel.buyerAggregate.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardTypeRepository extends JpaRepository<CardType, Long> {

}
