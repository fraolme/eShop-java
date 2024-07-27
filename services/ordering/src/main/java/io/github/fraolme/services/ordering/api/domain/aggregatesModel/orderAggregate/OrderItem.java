package io.github.fraolme.services.ordering.api.domain.aggregatesModel.orderAggregate;

import io.github.fraolme.services.ordering.api.domain.base.Entity;
import io.github.fraolme.services.ordering.utils.BigDecimalUtils;

import java.math.BigDecimal;

public class OrderItem extends Entity {
    private String productName;
    private String pictureUrl;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private Integer units;
    private Long productId;

    public OrderItem() {}

    public OrderItem(Long productId, String productName, BigDecimal unitPrice, BigDecimal discount,
                     String pictureUrl, Integer units) {
        if(units <= 0) {
            //TODO: throw new OrderingDomainException("Invalid number of units");
        }

         // unitPrice * units < discount
        if(BigDecimalUtils.lessThan(unitPrice.multiply(BigDecimal.valueOf(units)), discount)) {
            //TODO: throw new OrderingDomainException("The total of order item is lower than applied discount");
        }

        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.units = units;
        this.pictureUrl = pictureUrl;
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

    public void setNewDiscount(BigDecimal discount) {
        if(BigDecimalUtils.lessThan(discount, BigDecimal.valueOf(0))) {
            //TODO: throw new OrderingDomainException("Discount is not valid");
        }
        this.discount = discount;
    }

    public void addUnits(Integer units) {
        if(units < 0) {
            //TODO: throw new OrderingDomainException("Invalid units");
        }
        this.units += units;
    }
}
