package io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate;

import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.Buyer;
import io.github.fraolme.services.ordering.domain.aggregatesModel.buyerAggregate.PaymentMethod;
import io.github.fraolme.services.ordering.domain.base.Entity;
import io.github.fraolme.services.ordering.domain.base.IAggregateRoot;
import io.github.fraolme.services.ordering.utils.BigDecimalUtils;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

@Table(name = "orders")
@Inheritance
@jakarta.persistence.Entity
@NamedEntityGraph(name = "order_entity_graph", attributeNodes = {
        @NamedAttributeNode("orderStatus"),
        @NamedAttributeNode("orderItems")})
public class Order extends Entity implements IAggregateRoot {

    @Column(nullable = false)
    private ZonedDateTime orderDate;
    @Embedded
    private Address address;
    @ManyToOne(optional = false)
    private OrderStatus orderStatus;
    private String description;
    private boolean isDraft;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private final List<OrderItem> orderItems;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    // we will never access this, it is there just to create a foreign key relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "buyer_id")
    private Long buyerId;


    // we will never access this, it is there just to create a foreign key relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Buyer buyer;

    public Order(){
        this.orderItems = new ArrayList<>();
        this.isDraft = false;
    }

    public Order(String userId, String username, Address address, Integer cardTypeId, String cardNumber,
                 String cardSecurityNumber, String cardHolderName, ZonedDateTime cardExpiration, Long buyerId,
                 Long paymentMethodId){
        this();
        this.buyerId = buyerId;
        this.paymentMethodId = paymentMethodId;
        this.orderStatus = new OrderStatus(OrderStatus.submitted.getId());
        this.orderDate = ZonedDateTime.now();
        this.address = address;

        // add the OrderStartedDomainEvent to the domain events collection
        // to be raised/dispatched when commiting changes into the database
        this.addOrderStartedDomainEvent(userId, username, cardTypeId, cardNumber, cardSecurityNumber,
                cardHolderName, cardExpiration);
    }

    public static Order NewDraft() {
        var order = new Order();
        order.isDraft = true;
        return order;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public Address getAddress() {
        return address;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDraft() {
        return isDraft;
    }

    // returns unmodifiable list because only addOrderItem should be used to ass items
    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(this.orderItems);
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    // DDD Pattern
    // this Order AggregateRoot's method addOrderItem should be the only way to add items to the Order
    public void addOrderItem(Long productId, String productName, BigDecimal unitPrice, BigDecimal discount,
                             String pictureUrl, Integer units) {
        Optional<OrderItem> existingOrderForProduct = this.orderItems.stream().filter(o -> o.getProductId() == productId).findFirst();
        if(existingOrderForProduct.isPresent()) {
            var obj = existingOrderForProduct.get();
            // if previous order item exists for the same product modify it with higher discount
            if(BigDecimalUtils.greaterThan(discount, obj.getDiscount())) {
                obj.setNewDiscount(discount);
            }
            obj.addUnits(units);
        } else {
            // add validated new order item
            var orderItem = new OrderItem(this, productId, productName, unitPrice, discount, pictureUrl, units);
            this.orderItems.add(orderItem);
        }
    }

    public void setPaymentMethodId(Long id) {
        this.paymentMethodId = id;
    }

    public void setBuyerId(Long id) {
        this.buyerId = id;
    }

    // status updating functionality
    public void setAwaitingValidationStatus(){
        if(Objects.equals(this.orderStatus.getId(), OrderStatus.submitted.getId())) {
            //TODO: AddDomainEvent
            this.orderStatus = new OrderStatus(OrderStatus.awaitingValidation.getId());
        }
    }

    public void setStockConfirmedStatus(){
        if(Objects.equals(this.orderStatus.getId(), OrderStatus.awaitingValidation.getId())) {
            //TODO: AddDomainEvent
            this.orderStatus = new OrderStatus(OrderStatus.stockConfirmed.getId());
            description = "All the items were confirmed with available stock.";
        }
    }

    public void setPaidStatus(){
        if(Objects.equals(this.orderStatus.getId(), OrderStatus.stockConfirmed.getId())) {
            //TODO: AddDomainEvent
            this.orderStatus = new OrderStatus(OrderStatus.paid.getId());
            description = "The payment was performed at a simulated " +
                    "\"American Bank checking bank account ending on XX35071\"";
        }
    }

    public void setShippedStatus(){
        if(!Objects.equals(this.orderStatus.getId(), OrderStatus.paid.getId())) {
            this.statusChangeException(OrderStatus.shipped);
        }

        this.orderStatus = new OrderStatus(OrderStatus.shipped.getId());
        description = "The order was shipped";
        //TODO: AddDomainEvent
    }

    public void setCancelledStatus(){
        if(Objects.equals(this.orderStatus.getId(), OrderStatus.paid.getId()) ||
            Objects.equals(this.orderStatus.getId(), OrderStatus.shipped.getId())) {
            this.statusChangeException(OrderStatus.cancelled);
        }

        this.orderStatus = new OrderStatus(OrderStatus.cancelled.getId());
        this.description = "The order was cancelled";
        //TODO: AddDomainEvent
    }

    public void setCancelledStatusWhenStockIsRejected(List<Long> orderStockRejectedItems) {
        if(Objects.equals(this.orderStatus.getId(), OrderStatus.awaitingValidation.getId())) {
            this.orderStatus = new OrderStatus(OrderStatus.cancelled.getId());

            var itemsStockRejectedProductNames = this.orderItems.stream()
                    .filter(c -> orderStockRejectedItems.contains(c.getProductId()))
                    .map(OrderItem::getOrderItemProductName)
                    .reduce((a, b) -> a +  ", " + b);

            this.description = String.format("The product items don't have stock : (%s)",
                    itemsStockRejectedProductNames);
        }
    }

    private void addOrderStartedDomainEvent(String userId, String username, Integer cardTypeId, String cardNumber,
                                  String cardSecurityNumber, String cardHolderNumber, ZonedDateTime cardExpiration) {
        //TODO: add OrderStartedDomainEvent
    }

    private void statusChangeException(OrderStatus orderStatusToChange) {
        //TODO: throw new OrderingDomainException(String.format(
        // "Is not possible to change the order status from %s to %s",
        // this.orderStatus.getName(), orderStatusToChange.getName());
    }

    public BigDecimal getTotal() {
        return this.orderItems.stream().map(o -> o.getUnitPrice().multiply(BigDecimal.valueOf(o.getUnits())))
                .reduce(BigDecimal::add).orElse(BigDecimal.valueOf(0));
    }
}
