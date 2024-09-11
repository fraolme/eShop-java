package io.github.fraolme.services.catalog.entities;

import io.github.fraolme.services.catalog.exceptions.CatalogDomainException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@NamedEntityGraph(name = "catalog_item_entity_graph", attributeNodes = {
        @NamedAttributeNode("catalogBrand"),
        @NamedAttributeNode("catalogType")})
public class CatalogItem {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String pictureFileName;
    private String pictureUri;

    // Quantity in stock
    private int availableStock;
    // Available stock at which we should reorder
    private int restockThreshold;
    // Maximum number of units that can be in-stock at any time (due to physical/logistical constraints in warehouses)
    private int maxStockThreshold;
    /// True if item is on reorder
    private boolean onReorder;

    @ManyToOne(optional = false)
    private CatalogBrand catalogBrand;

    @ManyToOne(optional = false)
    private CatalogType catalogType;

    public CatalogItem() {}

    public CatalogItem(Long catalogTypeId, Long catalogBrandId, int availableStock, String description, String name, String price, String pictureFileName) {
        this.name = name;
        this.description = description;
        this.price = new BigDecimal(price);
        this.pictureFileName = pictureFileName;
        this.catalogType = new CatalogType(catalogTypeId);
        this.catalogBrand = new CatalogBrand(catalogBrandId);
        this.availableStock = availableStock;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public int getAvailableStock() {
        return availableStock;
    }

    public int getRestockThreshold() {
        return restockThreshold;
    }

    public int getMaxStockThreshold() {
        return maxStockThreshold;
    }

    public boolean isOnReorder() {
        return onReorder;
    }

    public CatalogBrand getCatalogBrand() {
        return catalogBrand;
    }

    public CatalogType getCatalogType() {
        return catalogType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public void setAvailableStock(int availableStock) {
        this.availableStock = availableStock;
    }

    public void setRestockThreshold(int restockThreshold) {
        this.restockThreshold = restockThreshold;
    }

    public void setMaxStockThreshold(int maxStockThreshold) {
        this.maxStockThreshold = maxStockThreshold;
    }

    public void setOnReorder(boolean onReorder) {
        this.onReorder = onReorder;
    }

    public void setCatalogBrand(CatalogBrand catalogBrand) {
        this.catalogBrand = catalogBrand;
    }

    public void setCatalogType(CatalogType catalogType) {
        this.catalogType = catalogType;
    }

    public int removeStock(int quantityDesired) throws CatalogDomainException {
        if(this.availableStock == 0) {
            throw new CatalogDomainException(String.format("Empty Stock, product item %s is sold out", this.name));
        }

        if(quantityDesired <= 0) {
            throw new CatalogDomainException("Item units desired should be greater than zero");
        }

        int removed = Math.min(quantityDesired, this.availableStock);
        this.availableStock -= removed;

        return removed;
    }

    public int addStock(int quantity) {
        int original = this.availableStock;

        if(this.availableStock + quantity > this.maxStockThreshold) {
            this.availableStock += (this.maxStockThreshold - this.availableStock);
        } else {
            this.availableStock += quantity;
        }
        this.onReorder = false;
        return this.availableStock - original;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatalogItem that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
