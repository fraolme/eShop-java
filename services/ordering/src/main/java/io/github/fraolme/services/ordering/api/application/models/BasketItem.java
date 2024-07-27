package io.github.fraolme.services.ordering.api.application.models;

import java.math.BigDecimal;
import java.util.Objects;

public class BasketItem {
    private String id;
    private Long productId;
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal oldUnitPrice;
    private Integer quantity;
    private String pictureUrl;

    public BasketItem() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getOldUnitPrice() {
        return oldUnitPrice;
    }

    public void setOldUnitPrice(BigDecimal oldUnitPrice) {
        this.oldUnitPrice = oldUnitPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketItem that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(productName, that.productName) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(oldUnitPrice, that.oldUnitPrice) && Objects.equals(quantity, that.quantity) && Objects.equals(pictureUrl, that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, productName, unitPrice, oldUnitPrice, quantity, pictureUrl);
    }

    public OrderItemDTO toOrderItemDTO() {
        return new OrderItemDTO(this.productId, this.productName, this.unitPrice, BigDecimal.ZERO, this.quantity, this.pictureUrl);
    }
}
