package io.github.fraolme.services.ordering.api.application.queries;

import io.github.fraolme.services.ordering.api.application.models.EnumerationViewModel;
import io.github.fraolme.services.ordering.api.application.models.OrderItemViewModel;
import io.github.fraolme.services.ordering.api.application.models.OrderSummaryViewModel;
import io.github.fraolme.services.ordering.api.application.models.OrderViewModel;
import io.github.fraolme.services.ordering.infrastructure.repositories.BuyerRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.CardTypeRepository;
import io.github.fraolme.services.ordering.infrastructure.repositories.OrderRepository;
import org.springframework.stereotype.Component;
import io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate.Order;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class OrderQueries {
    private final OrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final CardTypeRepository cardTypeRepository;

    public OrderQueries(OrderRepository orderRepository, BuyerRepository buyerRepository,
                        CardTypeRepository cardTypeRepository) {
        this.orderRepository = orderRepository;
        this.buyerRepository = buyerRepository;
        this.cardTypeRepository = cardTypeRepository;
    }

    public OrderViewModel getOrder(Long id) {
        var order = orderRepository.findById(id);
        return order.map(this::mapOrderToView).orElse(null);
    }

    public List<OrderSummaryViewModel> getOrdersByUser(UUID userId) {
        var buyer = buyerRepository.findByIdentityUuid(userId);
        if(buyer.isEmpty()) {
            return List.of();
        }
        var orders = orderRepository.findByBuyerId(buyer.get().getId());
        var summaries = new ArrayList<OrderSummaryViewModel>();
        for(var item: orders) {
            summaries.add(new OrderSummaryViewModel(item.getId(), item.getOrderDate(), item.getOrderStatus().getName(),
                    item.getTotal()));
        }

        return summaries;
    }

    public List<EnumerationViewModel> getCardTypes() {
        var cardTypes = cardTypeRepository.findAll();
        return cardTypes.stream().map(c -> new EnumerationViewModel(c.getId(), c.getName()))
                .toList();
    }

    private OrderViewModel mapOrderToView(Order order) {
        var orderItemViews = new ArrayList<OrderItemViewModel>();
        for(var item : order.getOrderItems()) {
            orderItemViews.add(
                    new OrderItemViewModel(
                        item.getOrderItemProductName(), item.getUnits(), item.getUnitPrice(), item.getPictureUrl()
                    ));
        }
        var address = order.getAddress();

        return new OrderViewModel(order.getId(), order.getOrderDate(), order.getOrderStatus().getName(),
                order.getDescription(), address.getStreet(), address.getCity(), address.getZipCode(),
                address.getCountry(), orderItemViews, order.getTotal());
    }
}
