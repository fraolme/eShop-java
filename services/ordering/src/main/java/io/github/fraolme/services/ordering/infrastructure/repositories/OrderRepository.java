package io.github.fraolme.services.ordering.infrastructure.repositories;

import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(value = "order_entity_graph")
    Optional<Order> findById(Long id);

    @EntityGraph(value = "order_entity_graph")
    List<Order> findByBuyerId(Long buyerId);
}
