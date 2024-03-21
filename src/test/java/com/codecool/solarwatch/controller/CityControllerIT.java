package com.codecool.solarwatch.controller;

import com.codecool.solarwatch.model.entity.city.City;
import com.codecool.solarwatch.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class CityControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityRepository cityRepository;


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createCity_WithUser_RoleUSER_ShouldReturnStatusCode403Forbidden() throws Exception {
        String requestBody = "{\n" +
                "    \"name\": \"Budapest\",\n" +
                "    \"lat\": 3.6,\n" +
                "    \"lon\": 2.5,\n" +
                "    \"state\": \"Pest county\",\n" +
                "    \"country\": \"Hungary\"\n" +
                "}";


        this.mockMvc.perform(post("/api/city").contentType(APPLICATION_JSON_UTF8)
                .content(requestBody))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void createCity_WithUser_RoleADMIN_ShouldReturnStatusOK() throws Exception {
        String requestBody = "{\n" +
                "    \"name\": \"Budapest\",\n" +
                "    \"lat\": 3.6,\n" +
                "    \"lon\": 2.5,\n" +
                "    \"state\": \"Pest county\",\n" +
                "    \"country\": \"Hungary\"\n" +
                "}";


        this.mockMvc.perform(post("/api/city").contentType(APPLICATION_JSON_UTF8)
                        .content(requestBody))
                .andExpect(status().isOk()).andExpect(content()
                        .string("City is saved!"));
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteCity_WithUser_RoleUser_ShouldReturnStatus403Forbidden() throws Exception {
        this.mockMvc.perform(delete("/api/city/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    void deleteCity_WithUser_RoleADMIN_ShouldReturnStatus200AndExpectedMessage() throws Exception {
        City expectedCity = new City(
                1,
                "Los Angeles",
                "California",
                "USA",
                1,
                1,
                List.of(),
                List.of()
        );

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(
                        expectedCity
                ));
        this.mockMvc.perform(delete("/api/city/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("City with id: 1 and all the references are deleted!"));
    }

}