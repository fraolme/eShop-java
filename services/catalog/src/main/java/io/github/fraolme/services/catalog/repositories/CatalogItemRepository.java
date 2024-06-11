package io.github.fraolme.services.catalog.repositories;

import io.github.fraolme.services.catalog.entities.CatalogItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CatalogItemRepository extends PagingAndSortingRepository<CatalogItem, Long>, JpaRepository<CatalogItem, Long> {

    //used entity graph to avoid N+1 statements
    @EntityGraph(value = "catalog_item_entity_graph")
    Page<CatalogItem> findAll(Pageable pageable);

    @EntityGraph(value = "catalog_item_entity_graph")
    Page<CatalogItem> findByNameStartingWithIgnoreCase(String name, Pageable pageable);

    @EntityGraph(value = "catalog_item_entity_graph")
    Page<CatalogItem> findByCatalogTypeIdAndCatalogBrandId(Long catalogTypeId, Long catalogBrandId, Pageable pageable);

    @EntityGraph(value = "catalog_item_entity_graph")
    Page<CatalogItem> findByCatalogBrandId(Long catalogBrandId, Pageable pageable);
}
