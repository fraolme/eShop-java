package io.github.fraolme.services.catalog.controllers;

import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping("catalog")
@RestController
public class CatalogController {

    private final CatalogItemRepository catalogItemRepository;

    public CatalogController(CatalogItemRepository catalogItemRepository) {
        this.catalogItemRepository = catalogItemRepository;
    }

    @GetMapping("items")
    public List<CatalogItem> getItems() {
        return catalogItemRepository.findAll();
    }
}
