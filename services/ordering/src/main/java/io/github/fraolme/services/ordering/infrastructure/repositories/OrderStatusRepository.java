package io.github.fraolme.services.ordering.infrastructure.repositories;

import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
