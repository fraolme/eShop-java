package io.github.fraolme.services.catalog;

import io.github.fraolme.services.catalog.controllers.CatalogController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CatalogApplicationTests {

	@Autowired
	private CatalogController catalogController;

	@Test
	void contextLoads() {
		assertThat(catalogController).isNotNull();
	}

}
