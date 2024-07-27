package io.github.fraolme.services.catalog;

import io.github.fraolme.services.catalog.controllers.CatalogController;
import io.github.fraolme.services.catalog.entities.CatalogItem;
import io.github.fraolme.services.catalog.repositories.CatalogBrandRepository;
import io.github.fraolme.services.catalog.repositories.CatalogItemRepository;
import io.github.fraolme.services.catalog.repositories.CatalogTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogController.class)
public class CatalogControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogItemRepository catalogItemRepositoryMock;

    @MockBean
    private CatalogTypeRepository catalogTypeRepositoryMock;

    @MockBean
    private CatalogBrandRepository catalogBrandRepositoryMock;

    @Test
    void getItemsShouldReturnCatalogItemsPage() throws Exception {
        // arrange
        List<CatalogItem> items = new ArrayList<CatalogItem>();
        items.add(new CatalogItem(2L, 2L, 100, ".NET Bot Black Hoodie", ".NET Bot Black Hoodie", "19.5", "1.png" ));
        var pageable = Pageable.ofSize(10).withPage(0);
        when(catalogItemRepositoryMock.findAll(pageable)).thenReturn(new PageImpl<CatalogItem>(items, pageable, 14));

        // act and assert
        this.mockMvc.perform(get("/catalog/items")).andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                        .andExpect(jsonPath("$.totalPages").value("2"))
                        .andExpect(jsonPath("$.totalElements").value("14"))
                        .andExpect(jsonPath("$.pageable.pageNumber").value("0"))
                        .andExpect(jsonPath("$.pageable.pageSize").value("10"))
                        .andExpect(jsonPath("$.content[0].catalogType.id").value("2"))
                        .andExpect(jsonPath("$.content[0].catalogBrand.id").value("2"))
                        .andExpect(jsonPath("$.content[0].name").value(".NET Bot Black Hoodie"))
                        .andExpect(jsonPath("$.content[0].description").value(".NET Bot Black Hoodie"));
    }

    @Test
    void getItemsByNameShouldReturn500WhenNameLengthIsLessThan2() throws Exception {
        assertThrows(Exception.class, () -> {
            this.mockMvc.perform(get("/catalog/items/byName/p"))
                    .andExpect(status().isInternalServerError());
        });
    }

    @Test
    void getItemByIdShouldReturnASingleCatalogItemWhenIdExists() throws Exception {
        var item = new CatalogItem(2L, 2L, 100, ".NET Bot Black Hoodie", ".NET Bot Black Hoodie", "19.5", "1.png" );
        item.setId(4L);
        when(catalogItemRepositoryMock.findById(4L)).thenReturn(Optional.of(item));

        this.mockMvc.perform(get("/catalog/items/4")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("4"));
    }

    @Test
    void getItemByIdShouldReturn404NotFoundWhenIdDoesNotExist() throws Exception {
        when(catalogItemRepositoryMock.findById(4L)).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/catalog/items/4"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemByIdShouldReturnBadRequestWhenPassingInvalidId() throws Exception {
        this.mockMvc.perform(get("/catalog/items/0"))
                .andExpect(status().isBadRequest());
    }
}
