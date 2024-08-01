package io.github.fraolme.services.ordering.domain.aggregatesModel.orderAggregate;

import io.github.fraolme.services.ordering.domain.base.Entity;
import io.github.fraolme.services.ordering.domain.exceptions.OrderingDomainException;
import io.github.fraolme.services.ordering.utils.BigDecimalUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Inheritance
@jakarta.persistence.Entity
public class OrderItem extends Entity {
    @Column(nullable = false)
    private String productName;
    private String pictureUrl;
    @Column(nullable = false)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    private BigDecimal discount;
    @Column(nullable = false)
    private Integer units;
    @Column(nullable = false)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public OrderItem() {}

    public OrderItem(Order order, Long productId, String productName, BigDecimal unitPrice, BigDecimal discount,
                     String pictureUrl, Integer units) throws OrderingDomainException {
        if(units <= 0) {
            throw new OrderingDomainException("Invalid number of units");
        }

         // unitPrice * units < discount
        if(BigDecimalUtils.lessThan(unitPrice.multiply(BigDecimal.valueOf(units)), discount)) {
            throw new OrderingDomainException("The total of order item is lower than applied discount");
        }

        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.units = units;
        this.pictureUrl = pictureUrl;
        this.order = order;
    }

    public String getOrderItemProductName() {
        return productName;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getUnits() {
        return units;
    }

    public Long getProductId() {
        return productId;
    }

    public void setNewDiscount(BigDecimal discount) throws OrderingDomainException {
        if(BigDecimalUtils.lessThan(discount, BigDecimal.valueOf(0))) {
            throw new OrderingDomainException("Discount is not valid");
        }
        this.discount = discount;
    }

    public void addUnits(Integer units) throws OrderingDomainException {
        if(units < 0) {
            throw new OrderingDomainException("Invalid units");
        }
        this.units += units;
    }
}
