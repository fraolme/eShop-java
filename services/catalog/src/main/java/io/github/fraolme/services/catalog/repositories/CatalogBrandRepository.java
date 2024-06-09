package io.github.fraolme.services.catalog.repositories;

import io.github.fraolme.services.catalog.entities.CatalogBrand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogBrandRepository extends JpaRepository<CatalogBrand, Long> {
}
