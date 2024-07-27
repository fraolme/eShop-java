package io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
