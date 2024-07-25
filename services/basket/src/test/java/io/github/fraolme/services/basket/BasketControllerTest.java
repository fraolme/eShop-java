package io.github.fraolme.services.basket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.fraolme.services.basket.controllers.BasketController;
import io.github.fraolme.services.basket.models.BasketCheckout;
import io.github.fraolme.services.basket.models.CustomerBasket;
import io.github.fraolme.services.basket.repositories.RedisBasketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BasketController.class)
public class BasketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RedisBasketRepository redisBasketRepository;

    @Test
    void getCustomerBasketSuccess() throws Exception {
        // arrange
        var fakeCustomerId = "1";
        var fakeCustomerBasket = new CustomerBasket(fakeCustomerId);
        when(redisBasketRepository.findById(fakeCustomerId)).thenReturn(Optional.of(fakeCustomerBasket));

        // act
        var actionResult = this.mockMvc.perform(
                get("/basket/"+ fakeCustomerId));

        // assert
        actionResult.andExpect(status().isOk());
        actionResult.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        actionResult.andExpect(jsonPath("$.buyerId").value(fakeCustomerId));
    }

    @Test
    void postCustomerBasketSuccess() throws Exception {
        // arrange
        var fakeCustomerId = "1";
        var fakeCustomerBasket = new CustomerBasket(fakeCustomerId);
        String json = new ObjectMapper().writeValueAsString(fakeCustomerBasket);
        when(redisBasketRepository.save(fakeCustomerBasket)).thenReturn(fakeCustomerBasket);

        // act
        var actionResult = this.mockMvc.perform(
                post("/basket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
        );

        // assert
        actionResult.andExpect(status().isOk());
        actionResult.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
        actionResult.andExpect(jsonPath("$.buyerId").value(fakeCustomerId));
    }

    @Test
    void checkoutWithoutBasketShouldReturnBadRequest() throws Exception {
        // arrange
        var fakeCustomerId = "2";
        when(redisBasketRepository.findById(fakeCustomerId)).thenReturn(Optional.empty());
        String json = new ObjectMapper()
                            .registerModule(new JavaTimeModule())
                            .writeValueAsString(new BasketCheckout("Addis Ababa", "Elm Street",
                "Addis Ababa", "Ethiopia", "0000", "999777", "Amaranta",
                ZonedDateTime.of(2026, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault()),
                "9875", 1, "2", UUID.randomUUID()));

        // act
        var actionResult = this.mockMvc.perform(
                post("/basket/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .characterEncoding("utf-8")
        );

        // assert
        actionResult.andExpect(status().isBadRequest());
    }
}
