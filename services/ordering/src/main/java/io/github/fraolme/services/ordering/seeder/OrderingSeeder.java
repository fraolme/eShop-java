package io.github.fraolme.services.ordering.seeder;

import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.Address;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.Order;
import io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate.OrderStatus;
import io.github.fraolme.services.ordering.api.domain.repositories.OrderRepository;
import io.github.fraolme.services.ordering.api.domain.repositories.OrderStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;

@Component
public class OrderingSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(OrderingSeeder.class);
    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;

    public OrderingSeeder(OrderRepository orderRepository, OrderStatusRepository orderStatusRepository) {
        this.orderRepository = orderRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding database");
        seed();
        log.info("seeded database");
        log.info("{} Order Statuses", this.orderStatusRepository.count());
        log.info("{} Orders", this.orderRepository.count());
    }

    private void seed() {
        if(this.orderStatusRepository.count() == 0) {
            seedOrderStatuses();
        }
        if(this.orderRepository.count() == 0) {
            seedOrders();
        }
    }

    private void seedOrderStatuses() {
        var statuses = new ArrayList<OrderStatus>();
        statuses.add(OrderStatus.submitted);
        statuses.add(OrderStatus.cancelled);
        statuses.add(OrderStatus.paid);
        statuses.add(OrderStatus.awaitingValidation);
        statuses.add(OrderStatus.shipped);
        statuses.add(OrderStatus.stockConfirmed);
        this.orderStatusRepository.saveAllAndFlush(statuses);
    }

    public void seedOrders() {
        var address = new Address("21st Street", "Addis Ababa", "Addis Ababa", "Ethiopia", "0000");
        var order = new Order("1", "john", address, 1, "VX234", "VVXM", "John Locke", ZonedDateTime.now().plusYears(1), null, null);
        order.addOrderItem(20L, "Dell Laptop", BigDecimal.valueOf(2300), BigDecimal.valueOf(429), "", 1);
        this.orderRepository.saveAndFlush(order);
    }
}
