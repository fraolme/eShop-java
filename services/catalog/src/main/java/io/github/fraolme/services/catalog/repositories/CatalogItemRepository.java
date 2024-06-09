package io.github.fraolme.services.catalog.repositories;

import io.github.fraolme.services.catalog.entities.CatalogItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {

    //used entity graph to avoid N+1 statements
    @EntityGraph(value = "catalog_item_entity_graph")
    List<CatalogItem> findAll();
}
