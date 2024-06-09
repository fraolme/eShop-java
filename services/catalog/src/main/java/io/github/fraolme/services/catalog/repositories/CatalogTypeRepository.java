package io.github.fraolme.services.catalog.repositories;

import io.github.fraolme.services.catalog.entities.CatalogType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogTypeRepository extends JpaRepository<CatalogType, Long> {
}
