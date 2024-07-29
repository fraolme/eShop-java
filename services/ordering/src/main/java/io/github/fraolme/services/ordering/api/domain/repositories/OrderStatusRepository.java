package io.github.fraolme.services.ordering.api.domain.repositories;

import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
