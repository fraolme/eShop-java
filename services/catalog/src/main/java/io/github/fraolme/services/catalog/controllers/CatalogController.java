package io.github.fraolme.services.catalog.controllers;

import io.github.fraolme.services.catalog.entities.CatalogBrand;
import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.entities.CatalogType;
import io.github.fraolme.services.catalog.integration_events.CatalogIntegrationEventService;
import io.github.fraolme.services.catalog.integration_events.events.ProductPriceChangedIntegrationEvent;
import io.github.fraolme.services.catalog.repositories.CatalogBrandRepository;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import io.github.fraolme.services.catalog.repositories.CatalogTypeRepository;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Validated
@RequestMapping("catalog")
@RestController
public class CatalogController {

    private final CatalogItemRepository catalogItemRepository;
    private final CatalogTypeRepository catalogTypeRepository;
    private final CatalogBrandRepository catalogBrandRepository;
    private final CatalogIntegrationEventService catalogIntegrationEventService;

    public CatalogController(CatalogItemRepository catalogItemRepository, CatalogTypeRepository catalogTypeRepository,
                             CatalogBrandRepository catalogBrandRepository,
                             CatalogIntegrationEventService catalogIntegrationEventService) {
        this.catalogItemRepository = catalogItemRepository;
        this.catalogTypeRepository = catalogTypeRepository;
        this.catalogBrandRepository = catalogBrandRepository;
        this.catalogIntegrationEventService = catalogIntegrationEventService;
    }

    @GetMapping("items")
    public Page<CatalogItem> getItems(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                      @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex) {
        //TODO: set pic uri
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageIndex);
        return catalogItemRepository.findAll(pageable);
    }

    @GetMapping("items/byName/{name}")
    public Page<CatalogItem> getItemsByName(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                            @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                            @PathVariable @Size(min = 2) String name) {
        //TODO: set pic uri
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageIndex);
        return  catalogItemRepository.findByNameStartingWithIgnoreCase(name, pageable);
    }

    @GetMapping("items/type/{catalogTypeId}/brand/{catalogBrandId}")
    public Page<CatalogItem> getItemsByTypeAndBrand(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                    @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                    @PathVariable Long catalogTypeId,
                                                    @PathVariable Long catalogBrandId) {
        //TODO: set pic uri
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageIndex);
        return  catalogItemRepository.findByCatalogTypeIdAndCatalogBrandId(catalogTypeId, catalogBrandId, pageable);
    }

    @GetMapping("items/type/all/brand/{catalogBrandId}")
    public Page<CatalogItem> getItemsByBrand(@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                    @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                                    @PathVariable Long catalogBrandId) {
        //TODO: set pic uri
        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageIndex);
        return  catalogItemRepository.findByCatalogBrandId(catalogBrandId, pageable);
    }

    @GetMapping("items/{id}")
    public CatalogItem getItemById(@PathVariable Long id) {
        if(id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //TODO: set pic uri
        return catalogItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("items")
    public CatalogItem createNewItem(@RequestBody CatalogItem item) {
        return this.catalogItemRepository.save(item);
    }

    @PutMapping("items")
    public CatalogItem updateProduct(@RequestBody CatalogItem updatedItem) {
        var existingItem = this.catalogItemRepository.findById(updatedItem.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var oldPrice = existingItem.getPrice();
        var raiseProductPriceChangedEvent = !oldPrice.equals(updatedItem.getPrice());

        // update current product
        existingItem.setName(updatedItem.getName());
        existingItem.setCatalogBrand(updatedItem.getCatalogBrand());
        existingItem.setCatalogType(updatedItem.getCatalogType());
        existingItem.setAvailableStock(updatedItem.getAvailableStock());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setDescription(updatedItem.getDescription());
        existingItem.setMaxStockThreshold(updatedItem.getMaxStockThreshold());
        existingItem.setPictureFileName(updatedItem.getPictureFileName());

        this.catalogItemRepository.save(existingItem);

        if(raiseProductPriceChangedEvent) {
            System.out.println("Here--------------------");
            // Create Integration Event to be published through Event bus
            var priceChangedEvent = new ProductPriceChangedIntegrationEvent(existingItem.getId(), updatedItem.getPrice(),
                    oldPrice);
            // Achieving atomicity between original Catalog database operation and the Integration Event Log
            catalogIntegrationEventService.saveEvent(priceChangedEvent);
            // Publish through the Event bus and mark the saved event as published
            catalogIntegrationEventService.publishThroughEventBus(priceChangedEvent);
        }

        return existingItem;
    }

    @DeleteMapping("{id}")
    public void deleteItem(@PathVariable Long id) {
        this.catalogItemRepository.deleteById(id);
    }

    @GetMapping("types")
    public List<CatalogType> getCatalogTypes() {
        return this.catalogTypeRepository.findAll();
    }

    @GetMapping("brands")
    public List<CatalogBrand> getCatalogBrands() {
        return  this.catalogBrandRepository.findAll();
    }
}
