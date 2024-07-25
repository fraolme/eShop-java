package io.github.fraolme.services.catalog.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

//TODO: add equals and hashcode
@Entity
public class CatalogType{
    @Id @GeneratedValue
    private Long id;
    private String type;

    public CatalogType() {}

    public CatalogType(String type) {
        this.type = type;
    }

    public CatalogType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
