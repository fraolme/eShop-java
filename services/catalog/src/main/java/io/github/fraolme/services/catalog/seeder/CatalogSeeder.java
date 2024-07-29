package io.github.fraolme.services.catalog.seeder;

import io.github.fraolme.services.catalog.entities.CatalogBrand;
import io.github.fraolme.services.catalog.entities.CatalogType;
import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.repositories.CatalogBrandRepository;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import io.github.fraolme.services.catalog.repositories.CatalogTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

//TODO: make it not run during unit tests
@Component
public class CatalogSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CatalogSeeder.class);

    private final CatalogItemRepository catalogItemRepository;
    private final CatalogTypeRepository catalogTypeRepository;
    private final CatalogBrandRepository catalogBrandRepository;

    @Autowired
    public CatalogSeeder(CatalogItemRepository catalogItemRepository, CatalogBrandRepository catalogBrandRepository,
                         CatalogTypeRepository catalogTypeRepository) {
        this.catalogItemRepository = catalogItemRepository;
        this.catalogBrandRepository = catalogBrandRepository;
        this.catalogTypeRepository = catalogTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding Database");
        seed();
        log.info("Seeded Database");
        log.info("{} Catalog Brands", this.catalogBrandRepository.count());
        log.info("{} Catalog Types", this.catalogTypeRepository.count());
        log.info("{} Catalog Items", this.catalogItemRepository.count());
    }

    private void seed() {
        if (this.catalogBrandRepository.count() == 0) {
            seedCatalogBrands();
        }

        if (this.catalogTypeRepository.count() == 0) {
            seedCatalogTypes();
        }

        if (this.catalogItemRepository.count() == 0) {
            seedCatalogItems();
        }
    }

    private void seedCatalogBrands() {
        var brands = new ArrayList<CatalogBrand>();
        brands.add(new CatalogBrand("Azure"));
        brands.add(new CatalogBrand(".NET"));
        brands.add(new CatalogBrand("Visual Studio"));
        brands.add(new CatalogBrand("SQL Server"));
        brands.add(new CatalogBrand("Other"));

        this.catalogBrandRepository.saveAllAndFlush(brands);
    }

    private void seedCatalogTypes() {
        var types = new ArrayList<CatalogType>();
        types.add(new CatalogType("Mug"));
        types.add(new CatalogType("T-Shirt"));
        types.add(new CatalogType("Sheet"));
        types.add(new CatalogType("USB Memory Stick"));

        this.catalogTypeRepository.saveAllAndFlush(types);
    }

    private void seedCatalogItems() {
        var items = new ArrayList<CatalogItem>();
        items.add(new CatalogItem(2L, 2L, 100, ".NET Bot Black Hoodie", ".NET Bot Black Hoodie", "19.5", "1.png" ));
        items.add(new CatalogItem(1L, 2L, 100, ".NET Black & White Mug", ".NET Black & White Mug", "8.50", "2.png" ));
        items.add(new CatalogItem(2L, 5L, 100, "Prism White T-Shirt", "Prism White T-Shirt", "12", "3.png" ));
        items.add(new CatalogItem(2L, 2L, 100, ".NET Foundation T-shirt", ".NET Foundation T-shirt", "12", "4.png" ));
        items.add(new CatalogItem(3L, 5L, 100, "Roslyn Red Sheet", "Roslyn Red Sheet", "8.5", "5.png" ));
        items.add(new CatalogItem(2L, 2L, 100, ".NET Blue Hoodie", ".NET Blue Hoodie", "12", "6.png" ));
        items.add(new CatalogItem(2L, 5L, 100, "Roslyn Red T-Shirt", "Roslyn Red T-Shirt", "12", "7.png" ));
        items.add(new CatalogItem(2L, 5L, 100, "Kudu Purple Hoodie", "Kudu Purple Hoodie", "8.5", "8.png" ));
        items.add(new CatalogItem(1L, 5L, 100, "Cup<T> White Mug", "Cup<T> White Mug", "12", "9.png" ));
        items.add(new CatalogItem(3L, 2L, 100, ".NET Foundation Sheet", ".NET Foundation Sheet", "12", "10.png" ));
        items.add(new CatalogItem(3L, 2L, 100, "Cup<T> Sheet", "Cup<T> Sheet", "8.5", "11.png" ));
        items.add(new CatalogItem(2L, 5L, 100, "Prism White TShirt", "Prism White TShirt", "12", "12.png" ));

        this.catalogItemRepository.saveAllAndFlush(items);
    }
}