package io.github.fraolme.services.catalog.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//TODO: add equals and hashcode
@Entity
public class CatalogBrand {
    @Id @GeneratedValue
    private Long id;
    private String brand;

    public CatalogBrand() {}

    public CatalogBrand(String brand) {
        this.brand = brand;
    }

    public CatalogBrand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }
}
