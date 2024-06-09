package io.github.fraolme.services.catalog;

import io.github.fraolme.services.catalog.controllers.CatalogController;
import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogController.class)
public class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogItemRepository catalogItemRepositoryMock;

    @Test
    void getCatalogItemsReturnsItems() throws Exception {
        var items = new ArrayList<CatalogItem>();
        items.add(new CatalogItem(2L, 2L, 100, ".NET Bot Black Hoodie", ".NET Bot Black Hoodie", "19.5", "1.png" ));
        when(catalogItemRepositoryMock.findAll()).thenReturn(items);

        this.mockMvc.perform(get("/catalog/items")).andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                        .andExpect(jsonPath("$[0].catalogType.id").value("2"))
                        .andExpect(jsonPath("$[0].catalogBrand.id").value("2"))
                        .andExpect(jsonPath("$[0].name").value(".NET Bot Black Hoodie"))
                        .andExpect(jsonPath("$[0].description").value(".NET Bot Black Hoodie"));
    }
}
