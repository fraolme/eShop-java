package io.github.fraolme.services.catalog.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;

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
    // Maximum number of units that can be in-stock at any time (due to physicial/logistical constraints in warehouses)
    private int maxStockThreshold;
    /// <summary>
    /// True if item is on reorder
    /// </summary>
    private boolean onReorder;

    @ManyToOne
    @JsonManagedReference
    private CatalogBrand catalogBrand;

    @ManyToOne
    @JsonManagedReference
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
}
