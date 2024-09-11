package io.github.fraolme.services.catalog.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CatalogBrand that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getBrand() {
        return brand;
    }
}
