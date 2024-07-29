package io.github.fraolme.services.ordering.api.domain.repositories;

import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
